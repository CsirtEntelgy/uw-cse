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

#include <stdint.h>
#include <boost/algorithm/string.hpp>
#include <boost/lexical_cast.hpp>
#include <map>
#include <string>
#include <vector>

#include "./HttpRequest.h"
#include "./HttpUtils.h"
#include "./HttpConnection.h"

using std::map;
using std::string;

namespace hw4 {

bool HttpConnection::GetNextRequest(HttpRequest *request) {
  // Use "WrappedRead" to read data into the buffer_
  // instance variable.  Keep reading data until either the
  // connection drops or you see a "\r\n\r\n" that demarcates
  // the end of the request header.
  //
  // Once you've seen the request header, use ParseRequest()
  // to parse the header into the *request argument.
  //
  // Very tricky part:  clients can send back-to-back requests
  // on the same socket.  So, you need to preserve everything
  // after the "\r\n\r\n" in buffer_ for the next time the
  // caller invokes GetNextRequest()!

  // STEP 1:
  size_t eof = buffer_.find("\r\n\r\n");
  if(eof == string::npos){
	  int bytes;
	  unsigned char buf[1024];
	  while(1){
		  bytes = WrappedRead(fd_, buf, 1024);
		  if(bytes == 0)
			  break;
		  else if(bytes == -1)
			  return false;
		  else{
			  buffer_ += string(reinterpret_cast<char*>(buf), bytes);
			  //check if reading should stop
			  eof = buffer_.find("\r\n\r\n");
			  if(eof != string::npos)
				  break;
		  }
	  }
  }
  
  //verify eof
  if(eof == string::npos)
	  return false;
  
  //verify buffer size
  if(buffer_.length() == 0)
	  return false;
  
  //another safety check for eof+buffer
  if(eof + 4 > buffer_.length())
	  return false;
  
  *request = ParseRequest(eof + 4);
  buffer_ = buffer_.substr(eof + 4);
  
  return true;
}

bool HttpConnection::WriteResponse(const HttpResponse &response) {
  std::string str = response.GenerateResponseString();
  int res = WrappedWrite(fd_,
                         (unsigned char *) str.c_str(),
                         str.length());
  if (res != static_cast<int>(str.length()))
    return false;
  return true;
}

HttpRequest HttpConnection::ParseRequest(size_t end) {
  HttpRequest req;
  req.URI = "/";  // by default, get "/".

  // Get the header.
  std::string str = buffer_.substr(0, end);

  // Split the header into lines.  Extract the URI from the first line
  // and store it in req.URI.  For each additional line beyond the
  // first, extract out the header name and value and store them in
  // req.headers (i.e., req.headers[headername] = headervalue).
  // You should look at HttpResponse.h for details about the HTTP header
  // format that you need to parse.
  //
  // You'll probably want to look up boost functions for (a) splitting
  // a string into lines on a "\r\n" delimiter, (b) trimming
  // whitespace from the end of a string, and (c) converting a string
  // to lowercase.

  // STEP 2:
  
  //splitting line-by-line
  std::vector<std::string> lines;
  
  //splitting on "\r\n"
  boost::iter_split(lines, str, boost::algorithm::first_finder("\r\n"));

  //trim whitespace
  for(uint32_t i = 0; i < lines.size(); i++)
	  boost::trim(lines[i]);
  
  //extract uri
  std::vector<std::string> temp;
  boost::split(temp, lines.front(), boost::is_any_of(" "));
  req.URI = temp.at(1);
  
  //extract and store headers
  for(uint32_t i = 1; i < lines.size(); i++){
	  std::vector<std::string> temp2;
	  boost::iter_split(temp2, lines[i], boost::algorithm::first_finder(": "));
	  boost::to_lower(temp2[0]);
	  req.headers[temp2.front()] = temp2.back();
  }
  
  return req;
}

}  // namespace hw4
