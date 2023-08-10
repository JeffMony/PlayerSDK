//
// Created by jeffli on 2023/8/10.
//

#ifndef ANDROIDMEDIACACHE_MEDIA_CACHE_REQUEST_INFO_H_
#define ANDROIDMEDIACACHE_MEDIA_CACHE_REQUEST_INFO_H_


typedef struct RequestInfo{
  std::string url;
  long range_start;
  long range_end;
} RequestInfo;


#endif //ANDROIDMEDIACACHE_MEDIA_CACHE_REQUEST_INFO_H_
