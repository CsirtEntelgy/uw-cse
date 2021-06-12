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

#include <boost/algorithm/string.hpp>
#include <iostream>
#include <memory>
#include <vector>
#include <sstream>

#include "./FileReader.h"
#include "./HttpConnection.h"
#include "./HttpRequest.h"
#include "./HttpUtils.h"
#include "./HttpServer.h"
#include "./libhw3/QueryProcessor.h"

using std::cerr;
using std::cout;
using std::endl;

namespace hw4 {

// This is the function that threads are dispatched into
// in order to process new client connections.
void HttpServer_ThrFn(ThreadPool::Task *t);

// Given a request, produce a response.
HttpResponse ProcessRequest(const HttpRequest &req,
                            const std::string &basedir,
                            const std::list<std::string> *indices);

// Process a file request.
HttpResponse ProcessFileRequest(const std::string &uri,
                                const std::string &basedir);

// Process a query request.
HttpResponse ProcessQueryRequest(const std::string &uri,
                                 const std::list<std::string> *indices);

bool HttpServer::Run(void) {
  // Create the server listening socket.
  int listen_fd;
  cout << "  creating and binding the listening socket..." << endl;
  if (!ss_.BindAndListen(AF_INET6, &listen_fd)) {
    cerr << endl << "Couldn't bind to the listening socket." << endl;
    return false;
  }

  // Spin, accepting connections and dispatching them.  Use a
  // threadpool to dispatch connections into their own thread.
  cout << "  accepting connections..." << endl << endl;
  ThreadPool tp(kNumThreads);
  while (1) {
    HttpServerTask *hst = new HttpServerTask(HttpServer_ThrFn);
    hst->basedir = staticfile_dirpath_;
    hst->indices = &indices_;
    if (!ss_.Accept(&hst->client_fd,
                    &hst->caddr,
                    &hst->cport,
                    &hst->cdns,
                    &hst->saddr,
                    &hst->sdns)) {
      // The accept failed for some reason, so quit out of the server.
      // (Will happen when kill command is used to shut down the server.)
      break;
    }
    // The accept succeeded; dispatch it.
    tp.Dispatch(hst);
  }
  return true;
}

void HttpServer_ThrFn(ThreadPool::Task *t) {
  // Cast back our HttpServerTask structure with all of our new
  // client's information in it.
  std::unique_ptr<HttpServerTask> hst(static_cast<HttpServerTask *>(t));
  cout << "  client " << hst->cdns << ":" << hst->cport << " "
       << "(IP address " << hst->caddr << ")" << " connected." << endl;

  // Use the HttpConnection class to read in the next request from
  // this client, process it by invoking ProcessRequest(), and then
  // use the HttpConnection class to write the response.  If the
  // client sent a "Connection: close\r\n" header, then shut down
  // the connection.

  // STEP 1:

  bool done = false;
  while (!done) {
	//create connection
	HttpConnection conn(hst->client_fd);
	//create request
	HttpRequest req;
	
	//read next request
	if(!conn.GetNextRequest(&req)){
		close(hst->client_fd);
		done = true;
	}
	
	//processing request
	HttpResponse resp = ProcessRequest(req, hst->basedir, hst->indices);
	
	//use connection to output response
	if(!conn.WriteResponse(resp)){
		close(hst->client_fd);
		done = true;
	}
	
	//if shutdown requested, shutdown
	if(req.headers["connection"] == "close"){
		close(hst->client_fd);
		done = true;
	}
  }
}

HttpResponse ProcessRequest(const HttpRequest &req,
                            const std::string &basedir,
                            const std::list<std::string> *indices) {
  // Is the user asking for a static file?
  if (req.URI.substr(0, 8) == "/static/") {
    return ProcessFileRequest(req.URI, basedir);
  }

  // The user must be asking for a query.
  return ProcessQueryRequest(req.URI, indices);
}


HttpResponse ProcessFileRequest(const std::string &uri,
                                const std::string &basedir) {
  // The response we'll build up.
  HttpResponse ret;

  // Steps to follow:
  //  - use the URLParser class to figure out what filename
  //    the user is asking for.
  //
  //  - use the FileReader class to read the file into memory
  //
  //  - copy the file content into the ret.body
  //
  //  - depending on the file name suffix, set the response
  //    Content-type header as appropriate, e.g.,:
  //      --> for ".html" or ".htm", set to "text/html"
  //      --> for ".jpeg" or ".jpg", set to "image/jpeg"
  //      --> for ".png", set to "image/png"
  //      etc.
  //
  // be sure to set the response code, protocol, and message
  // in the HttpResponse as well.
  std::string fname = "";

  // STEP 2:

  //parse uri
  URLParser up;
  up.Parse(uri);
  fname += up.get_path();
  
  //remove "static"
  fname = fname.replace(0, 8, "");
  FileReader fr(basedir, fname);
  
  if(fr.ReadFile(&ret.body)){
	  //fetch file name suffix
	  size_t t1 = fname.rfind(".");
	  std::string suffix = fname.substr(t1, fname.length() - 1);
	  
	  //set content type accordingly
	  if (suffix == ".html" || suffix == ".htm")
		ret.headers["Content-type"] = "text/html";
	  else if (suffix == ".jpg" || suffix == ".jpeg")
		ret.headers["Content-type"] = "image/jpeg";
	  else if (suffix == ".txt" || suffix == ".")
		ret.headers["Content-type"] = "text/plain";
      else if (suffix == ".js")
		ret.headers["Content-type"] = "text/javascript";
	  else if (suffix == ".csv")
		ret.headers["Content-type"] = "text/csv";
      else if (suffix == ".css")
		ret.headers["Content-type"] = "text/css";
      else if (suffix == ".xml")
		ret.headers["Content-type"] = "text/xml";
      else if (suffix == ".png")
		ret.headers["Content-type"] = "image/png";
      else if (suffix == ".tiff")
		ret.headers["Content-type"] = "image/tiff";
      else if (suffix == ".gif")
		ret.headers["Content-type"] = "image/gif";
      else
		ret.headers["Content-type"] = "application/octet-stream";
	
	  //set response protocol, code, message
	  ret.protocol = "HTTP/1.1";
	  ret.response_code = 200;
	  ret.message = "OK";
	  return ret;
  }

  // If you couldn't find the file, return an HTTP 404 error.
  ret.protocol = "HTTP/1.1";
  ret.response_code = 404;
  ret.message = "Not Found";
  ret.body = "<html><body>Couldn't find file \"";
  ret.body +=  EscapeHTML(fname);
  ret.body += "\"</body></html>";
  return ret;
}

HttpResponse ProcessQueryRequest(const std::string &uri,
                                 const std::list<std::string> *indices) {
  // The response we're building up.
  HttpResponse ret;

  // Your job here is to figure out how to present the user with
  // the same query interface as our solution_binaries/http333d server.
  // A couple of notes:
  //
  //  - no matter what, you need to present the 333gle logo and the
  //    search box/button
  //
  //  - if the user had previously typed in a search query, you also
  //    need to display the search results.
  //
  //  - you'll want to use the URLParser to parse the uri and extract
  //    search terms from a typed-in search query.  convert them
  //    to lower case.
  //
  //  - you'll want to create and use a hw3::QueryProcessor to process
  //    the query against the search indices
  //
  //  - in your generated search results, see if you can figure out
  //    how to hyperlink results to the file contents, like we did
  //    in our solution_binaries/http333d.

  // STEP 3:
  
  //create 333gle logo / searchbox / button
  ret.body  = "<html><head><title>333gle</title></head>\r\n";
  ret.body += "<body>\r\n";
  ret.body += "<center style=\"font-size:450%;\">\r\n";
  ret.body += "<span style=\"position:relative;bottom:-0.33em;color:orange;\">";
  ret.body += "3</span>";
  ret.body += "<span style=\"color:red;\">3</span>";
  ret.body += "<span style=\"color:gold;\">3</span>";
  ret.body += "<span style=\"color:blue;\">g</span>";
  ret.body += "<span style=\"color:green;\">l</span>";
  ret.body += "<span style=\"color:red;\">e</span>\r\n";
  ret.body += "</center>\r\n";
  ret.body += "<p>\r\n";
  ret.body += "<div style=\"height:10px;\"></div>\r\n";
  ret.body += "<center>\r\n";
  ret.body += "<form action=\"/query\" method=\"get\">\r\n";
  ret.body += "<input type=\"text\" size=30 name=\"terms\" />\r\n";
  ret.body += "<input type=\"submit\" value=\"Search\" />\r\n";
  ret.body += "</form>\r\n";
  ret.body += "</center><p>\r\n";

  //parse uri
  URLParser up;
  up.Parse(uri);
  std::string q = up.get_args()["terms"];
  boost::trim(q);
  boost::to_lower(q);
  
  //display search results
  if(uri.find("query?terms=") != std::string::npos){
	//split into words
    std::vector<std::string> words;
    boost::split(words, q, boost::is_any_of(" "));
	//process query
	hw3::QueryProcessor qp(*indices, false);
	//search for match
	std::vector<hw3::QueryProcessor::QueryResult> qr = qp.ProcessQuery(words);
	
	//classify
	if(qr.size() == 0){
		//no match
		ret.body += "<p><br>\r\n";
		ret.body += "No results found for <b>";
		ret.body += EscapeHTML(q);
		ret.body += "</b>\r\n";
		ret.body += "<p>\r\n";
		ret.body += "\r\n";
	}else{
		//match
		//display num of results found
		std::stringstream s;
		ret.body += "<p><br>\r\n";
		if (qr.size() <= 1)
			s << "1 result found for <b>";
		else
			s << qr.size() << "Results found for <b>";
		ret.body += s.str();
		s.str("");
		ret.body += EscapeHTML(q);
		ret.body += "</b>\r\n";
		ret.body += "<p>\r\n\r\n";
		
		//display matched docs with hyperlink
		ret.body += "<ul>\r\n";
		for(HWSize_t i = 0; i < qr.size(); i++){
			ret.body += " <li> <a href=\"";
			//check static
			if(qr[i].document_name.substr(0,7) != "http://"){
				ret.body += "/static/";
				ret.body += qr[i].document_name;
			}else
				ret.body += EscapeHTML(qr[i].document_name);
			ret.body += "\">";
			ret.body += qr[i].document_name;
			ret.body += "</a> [";
			ret.body += std::to_string(qr[i].rank);
			ret.body += "]<br>\r\n";
		}
		ret.body += "</ul>\r\n";
	}
  }
  
  //eof
  ret.body += "</body>\r\n";
  ret.body += "</html>\r\n";
  
  //set response protocol, code, message, like the old days! phew... finally!!
  ret.protocol = "HTTP/1.1";
  ret.response_code = 200;
  ret.message = "OK";
  
  return ret;
}

}  // namespace hw4
