//
// Created by jeffli on 2023/8/10.
//

#include "request_parser.h"
#include "b64.h"
#include <regex>

RequestParser::RequestParser(const char *data)
  : request_info_(nullptr) {
  Parse(data);
}

RequestParser::~RequestParser() {
  if (request_info_) {
    delete request_info_;
    request_info_ = nullptr;
  }
}

std::string RequestParser::GetUrl() {
  if (request_info_) {
    return request_info_->url;
  }
  return "";
}

long RequestParser::GetRangeStart() {
  if (request_info_) {
    return request_info_->range_start;
  }
  return -1;
}

long RequestParser::GetRangeEnd() {
  if (request_info_) {
    return request_info_->range_end;
  }
  return -1;
}

void RequestParser::Parse(const char *data) {
  request_info_ = new RequestInfo();
  request_info_->url.clear();
  request_info_->range_start = -1;
  request_info_->range_end = -1;
  std::regex r("GET /(.*) HTTP");
  std::smatch m;
  std::string origin;
  origin.append(data);
  if (std::regex_search(origin.cbegin(), origin.cend(), m, r)) {
    std::string m_str = m.str();
    int length = m_str.length();
    std::string result = m_str.substr(5, length - 10);
    unsigned char *decode_str = b64_decode(result.c_str(), result.size());
    request_info_->url.append(reinterpret_cast<const char *>(decode_str));
  }
}
