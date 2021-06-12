/*
 * Copyright ©2019 Aaron Johnston.  All rights reserved.  Permission is
 * hereby granted to students registered for University of Washington
 * CSE 333 for use solely during Summer Quarter 2019 for purposes of
 * the course.  No other use, copying, distribution, or modification
 * is permitted without prior written consent. Copyrights for
 * third-party components of this work must be honored.  Instructors
 * interested in reusing these course materials should contact the
 * author.
 */
 
 /*
 Young Bin Cho
 1773497
 joshua97@uw.edu
 */

#include <stdio.h>       // for snprintf()
#include <unistd.h>      // for close(), fcntl()
#include <sys/types.h>   // for socket(), getaddrinfo(), etc.
#include <sys/socket.h>  // for socket(), getaddrinfo(), etc.
#include <arpa/inet.h>   // for inet_ntop()
#include <netdb.h>       // for getaddrinfo()
#include <errno.h>       // for errno, used by strerror()
#include <string.h>      // for memset, strerror()
#include <iostream>      // for std::cerr, etc.

#include "./ServerSocket.h"

extern "C" {
  #include "libhw1/CSE333.h"
}

namespace hw4 {

ServerSocket::ServerSocket(uint16_t port) {
  port_ = port;
  listen_sock_fd_ = -1;
}

ServerSocket::~ServerSocket() {
  // Close the listening socket if it's not zero.  The rest of this
  // class will make sure to zero out the socket if it is closed
  // elsewhere.
  if (listen_sock_fd_ != -1)
    close(listen_sock_fd_);
  listen_sock_fd_ = -1;
}

bool ServerSocket::BindAndListen(int ai_family, int *listen_fd) {
  // Use "getaddrinfo," "socket," "bind," and "listen" to
  // create a listening socket on port port_.  Return the
  // listening socket through the output parameter "listen_fd".

  // STEP 1:
  
  //temp variable: "t1" | used to fetch addr structs
  struct addrinfo t1;
  memset(&t1, 0, sizeof(struct addrinfo));

  //validate input
  if(ai_family != AF_UNSPEC && ai_family != AF_INET && ai_family != AF_INET6)
	  return false;
  
  //populate t1
  t1.ai_family = ai_family;
  t1.ai_socktype = SOCK_STREAM;
  t1.ai_flags = AI_PASSIVE;
  t1.ai_protocol = IPPROTO_TCP;
  t1.ai_canonname = NULL;
  t1.ai_addr = NULL;
  t1.ai_next = NULL;
  
  //temp variable: "t2" | Output param | addrinfo struct
  struct addrinfo* t2;
  
  //get,set port
  std::string port = std::to_string(port_);
  //grab addr structs
  int res = getaddrinfo(NULL, port.c_str(), &t1, & t2);
  //validate res
  if(res != 0)
	  return false;
  
  //loop until we find a valid socket
  for(struct addrinfo* i = t2; i != NULL; i = i ->ai_next){
	  //set lsf
	  listen_sock_fd_ = socket(i->ai_family, i->ai_socktype, i->ai_protocol);
	  //validate lsf
	  if(listen_sock_fd_ == -1)
		  continue;
	  
	  int opt = 1;
	  setsockopt(listen_sock_fd_, SOL_SOCKET, SO_REUSEADDR, &opt, sizeof(opt));
	  
	  //try bind, break on successful bind
	  if(bind(listen_sock_fd_, i->ai_addr, i->ai_addrlen) == 0){
		  sock_family_ = i->ai_family;
		  break;
	  }
	  
	  //clean up
	  close(listen_sock_fd_);
	  //failed case
	  listen_sock_fd_ = -1;
  }
  
  //free
  freeaddrinfo(t2);
  
  //return false on failure to bind
  if(listen_sock_fd_ == -1)
	  return false;
  
  //sucessful bind case
  if(listen(listen_sock_fd_, SOMAXCONN) != 0){
	  close(listen_sock_fd_);
	  return -1;
  }
  
  //set output
  *listen_fd = listen_sock_fd_;

  return true;
}

bool ServerSocket::Accept(int *accepted_fd,
                          std::string *client_addr,
                          uint16_t *client_port,
                          std::string *client_dnsname,
                          std::string *server_addr,
                          std::string *server_dnsname) {
  // Accept a new connection on the listening socket listen_sock_fd_.
  // (Block until a new connection arrives.)  Return the newly accepted
  // socket, as well as information about both ends of the new connection,
  // through the various output parameters.

  // STEP 2:
  struct sockaddr_storage caddr;
  socklen_t caddr_len = sizeof(caddr);
  struct sockaddr* addr = reinterpret_cast<struct sockaddr *>(&caddr);
  
  while(1){
	  //set client
	  int client_fd = accept(listen_sock_fd_, addr, &caddr_len);
	  //validate client
	  if(client_fd < 0){
		  if(errno == EAGAIN || errno == EINTR)
			  continue;
		  return false;
	  }
	  
	  *accepted_fd = client_fd;
	  
	  //fetch client info: IP addr | port
	  if(addr->sa_family == AF_INET){
		  char str[INET_ADDRSTRLEN];
		  struct sockaddr_in* i4 = reinterpret_cast<struct sockaddr_in*>(addr);
		  inet_ntop(AF_INET, &(i4->sin_addr), str, INET_ADDRSTRLEN);
		  *client_addr = std::string(str);
		  *client_port = htons(i4->sin_port);
	  }else{
		  char str[INET6_ADDRSTRLEN];
		  struct sockaddr_in6* i6 = reinterpret_cast<struct sockaddr_in6*>(addr);
		  inet_ntop(AF_INET6, &(i6->sin6_addr), str, INET6_ADDRSTRLEN);
		  *client_addr = std::string(str);
		  *client_port = htons(i6->sin6_port);
	  }
	  
	  //fetch client info: DNS
	  char host[1024];
	  //validate DNS
	  if(getnameinfo(addr, caddr_len, host, 1024, NULL, 0, 0) != 0)
		  return false;
	  *client_dnsname = host;
	  
	  //fetch client info: server
	  char svr[1024];
	  svr[0] = '\0';
	  if(addr->sa_family == AF_INET){
		  struct sockaddr_in srvr;
		  socklen_t srvrlen = sizeof(srvr);
		  char buf[INET_ADDRSTRLEN];
		  getsockname(client_fd, (struct sockaddr*)&srvr, &srvrlen);
		  inet_ntop(AF_INET, &srvr.sin_addr, buf, INET_ADDRSTRLEN);
		  getnameinfo((const struct sockaddr*)&srvr, srvrlen, svr, 1024, NULL, 0, 0);
		  *server_addr = buf;
		  *server_dnsname = svr;
	  }else{
		  struct sockaddr_in6 srvr;
		  socklen_t srvrlen = sizeof(srvr);
		  char buf[INET6_ADDRSTRLEN];
		  getsockname(client_fd, (struct sockaddr*)&srvr, &srvrlen);
		  inet_ntop(AF_INET6, &srvr.sin6_addr, buf, INET6_ADDRSTRLEN);
		  getnameinfo((const struct sockaddr*)&srvr, srvrlen, svr, 1024, NULL, 0, 0);
		  *server_addr = buf;
		  *server_dnsname = svr;
	  }
	  
	  //break infinite loop
	  break;
  }
  return true;
}

}  // namespace hw4
