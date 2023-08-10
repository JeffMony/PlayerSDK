//
// Created by jeffli on 2023/8/10.
//

#ifndef ANDROIDMEDIACACHE_MEDIA_CACHE_REQUEST_PARSER_H_
#define ANDROIDMEDIACACHE_MEDIA_CACHE_REQUEST_PARSER_H_

#include <string>
#include "request_info.h"

class RequestParser {
 public:
  RequestParser(const char *data);

  virtual ~RequestParser();

  std::string GetUrl();

  long GetRangeStart();

  long GetRangeEnd();

 private:
  void Parse(const char *data);

 private:
  RequestInfo *request_info_;



};

#endif //ANDROIDMEDIACACHE_MEDIA_CACHE_REQUEST_PARSER_H_
