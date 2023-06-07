/*
 *  Copyright 2012 The LibYuv Project Authors. All rights reserved.
 *
 *  Use of this source code is governed by a BSD-style license
 *  that can be found in the LICENSE file in the root of the source
 *  tree. An additional intellectual property rights grant can be found
 *  in the file PATENTS. All contributing project authors may
 *  be found in the AUTHORS file in the root of the source tree.
 */

#include "libyuv/row.h"

#include "libyuv/basic_types.h"

#ifdef __cplusplus
namespace libyuv {
extern "C" {
#endif

// YUV to RGB does multiple of 8 with SIMD and remainder with C.
#define YANY(NAMEANY, I420TORGB_SIMD, I420TORGB_C, UV_SHIFT, BPP, MASK)        \
    void NAMEANY(const uint8* y_buf,                                           \
                 const uint8* u_buf,                                           \
                 const uint8* v_buf,                                           \
                 uint8* rgb_buf,                                               \
                 int width) {                                                  \
      int n = width & ~MASK;                                                   \
      if (n > 0) {                                                             \
        I420TORGB_SIMD(y_buf, u_buf, v_buf, rgb_buf, n);                       \
      }                                                                        \
      I420TORGB_C(y_buf + n,                                                   \
                  u_buf + (n >> UV_SHIFT),                                     \
                  v_buf + (n >> UV_SHIFT),                                     \
                  rgb_buf + n * BPP, width & MASK);                            \
    }

#ifdef HAS_I422TOARGBROW_SSSE3
YANY(I422ToARGBRow_Any_SSSE3, I422ToARGBRow_SSSE3, I422ToARGBRow_C,
     1, 4, 7)
#endif  // HAS_I422TOARGBROW_SSSE3
#ifdef HAS_I444TOARGBROW_SSSE3
YANY(I444ToARGBRow_Any_SSSE3, I444ToARGBRow_SSSE3, I444ToARGBRow_C,
     0, 4, 7)
YANY(I411ToARGBRow_Any_SSSE3, I411ToARGBRow_SSSE3, I411ToARGBRow_C,
     2, 4, 7)
YANY(I422ToBGRARow_Any_SSSE3, I422ToBGRARow_SSSE3, I422ToBGRARow_C,
     1, 4, 7)
YANY(I422ToABGRRow_Any_SSSE3, I422ToABGRRow_SSSE3, I422ToABGRRow_C,
     1, 4, 7)
YANY(I422ToRGBARow_Any_SSSE3, I422ToRGBARow_SSSE3, I422ToRGBARow_C,
     1, 4, 7)
// I422ToRGB565Row_SSSE3 is unaligned.
YANY(I422ToARGB4444Row_Any_SSSE3, I422ToARGB4444Row_SSSE3, I422ToARGB4444Row_C,
     1, 2, 7)
YANY(I422ToARGB1555Row_Any_SSSE3, I422ToARGB1555Row_SSSE3, I422ToARGB1555Row_C,
     1, 2, 7)
YANY(I422ToRGB565Row_Any_SSSE3, I422ToRGB565Row_SSSE3, I422ToRGB565Row_C,
     1, 2, 7)
// I422ToRGB24Row_SSSE3 is unaligned.
YANY(I422ToRGB24Row_Any_SSSE3, I422ToRGB24Row_SSSE3, I422ToRGB24Row_C, 1, 3, 7)
YANY(I422ToRAWRow_Any_SSSE3, I422ToRAWRow_SSSE3, I422ToRAWRow_C, 1, 3, 7)
YANY(I422ToYUY2Row_Any_SSE2, I422ToYUY2Row_SSE2, I422ToYUY2Row_C, 1, 2, 15)
YANY(I422ToUYVYRow_Any_SSE2, I422ToUYVYRow_SSE2, I422ToUYVYRow_C, 1, 2, 15)
#endif  // HAS_I444TOARGBROW_SSSE3
#ifdef HAS_I422TOARGBROW_AVX2
YANY(I422ToARGBRow_Any_AVX2, I422ToARGBRow_AVX2, I422ToARGBRow_C, 1, 4, 15)
#endif  // HAS_I422TOARGBROW_AVX2
#ifdef HAS_I422TOBGRAROW_AVX2
YANY(I422ToBGRARow_Any_AVX2, I422ToBGRARow_AVX2, I422ToBGRARow_C, 1, 4, 15)
#endif  // HAS_I422TOBGRAROW_AVX2
#ifdef HAS_I422TORGBAROW_AVX2
YANY(I422ToRGBARow_Any_AVX2, I422ToRGBARow_AVX2, I422ToRGBARow_C, 1, 4, 15)
#endif  // HAS_I422TORGBAROW_AVX2
#ifdef HAS_I422TOABGRROW_AVX2
YANY(I422ToABGRRow_Any_AVX2, I422ToABGRRow_AVX2, I422ToABGRRow_C, 1, 4, 15)
#endif  // HAS_I422TOABGRROW_AVX2
#ifdef HAS_I422TOARGBROW_NEON
YANY(I444ToARGBRow_Any_NEON, I444ToARGBRow_NEON, I444ToARGBRow_C, 0, 4, 7)
YANY(I422ToARGBRow_Any_NEON, I422ToARGBRow_NEON, I422ToARGBRow_C, 1, 4, 7)
YANY(I411ToARGBRow_Any_NEON, I411ToARGBRow_NEON, I411ToARGBRow_C, 2, 4, 7)
YANY(I422ToBGRARow_Any_NEON, I422ToBGRARow_NEON, I422ToBGRARow_C, 1, 4, 7)
YANY(I422ToABGRRow_Any_NEON, I422ToABGRRow_NEON, I422ToABGRRow_C, 1, 4, 7)
YANY(I422ToRGBARow_Any_NEON, I422ToRGBARow_NEON, I422ToRGBARow_C, 1, 4, 7)
YANY(I422ToRGB24Row_Any_NEON, I422ToRGB24Row_NEON, I422ToRGB24Row_C, 1, 3, 7)
YANY(I422ToRAWRow_Any_NEON, I422ToRAWRow_NEON, I422ToRAWRow_C, 1, 3, 7)
YANY(I422ToARGB4444Row_Any_NEON, I422ToARGB4444Row_NEON, I422ToARGB4444Row_C,
     1, 2, 7)
YANY(I422ToARGB1555Row_Any_NEON, I422ToARGB1555Row_NEON, I422ToARGB1555Row_C,
     1, 2, 7)
YANY(I422ToRGB565Row_Any_NEON, I422ToRGB565Row_NEON, I422ToRGB565Row_C, 1, 2, 7)
#endif  // HAS_I422TOARGBROW_NEON
#ifdef HAS_I422TOYUY2ROW_NEON
YANY(I422ToYUY2Row_Any_NEON, I422ToYUY2Row_NEON, I422ToYUY2Row_C, 1, 2, 15)
#endif  // HAS_I422TOYUY2ROW_NEON
#ifdef HAS_I422TOUYVYROW_NEON
YANY(I422ToUYVYRow_Any_NEON, I422ToUYVYRow_NEON, I422ToUYVYRow_C, 1, 2, 15)
#endif  // HAS_I422TOUYVYROW_NEON
#undef YANY

// Wrappers to handle odd width
#define NV2NY(NAMEANY, NV12TORGB_SIMD, NV12TORGB_C, UV_SHIFT, BPP)             \
    void NAMEANY(const uint8* y_buf,                                           \
                 const uint8* uv_buf,                                          \
                 uint8* rgb_buf,                                               \
                 int width) {                                                  \
      int n = width & ~7;                                                      \
      if (n > 0) {                                                             \
        NV12TORGB_SIMD(y_buf, uv_buf, rgb_buf, n);                             \
      }                                                                        \
      NV12TORGB_C(y_buf + n,                                                   \
                  uv_buf + (n >> UV_SHIFT),                                    \
                  rgb_buf + n * BPP, width & 7);                               \
    }

#ifdef HAS_NV12TOARGBROW_SSSE3
NV2NY(NV12ToARGBRow_Any_SSSE3, NV12ToARGBRow_SSSE3, NV12ToARGBRow_C,
      0, 4)
NV2NY(NV21ToARGBRow_Any_SSSE3, NV21ToARGBRow_SSSE3, NV21ToARGBRow_C,
      0, 4)
#endif  // HAS_NV12TOARGBROW_SSSE3
#ifdef HAS_NV12TOARGBROW_NEON
NV2NY(NV12ToARGBRow_Any_NEON, NV12ToARGBRow_NEON, NV12ToARGBRow_C, 0, 4)
NV2NY(NV21ToARGBRow_Any_NEON, NV21ToARGBRow_NEON, NV21ToARGBRow_C, 0, 4)
#endif  // HAS_NV12TOARGBROW_NEON
#ifdef HAS_NV12TORGB565ROW_SSSE3
NV2NY(NV12ToRGB565Row_Any_SSSE3, NV12ToRGB565Row_SSSE3, NV12ToRGB565Row_C,
      0, 2)
NV2NY(NV21ToRGB565Row_Any_SSSE3, NV21ToRGB565Row_SSSE3, NV21ToRGB565Row_C,
      0, 2)
#endif  // HAS_NV12TORGB565ROW_SSSE3
#ifdef HAS_NV12TORGB565ROW_NEON
NV2NY(NV12ToRGB565Row_Any_NEON, NV12ToRGB565Row_NEON, NV12ToRGB565Row_C, 0, 2)
NV2NY(NV21ToRGB565Row_Any_NEON, NV21ToRGB565Row_NEON, NV21ToRGB565Row_C, 0, 2)
#endif  // HAS_NV12TORGB565ROW_NEON
#undef NVANY

#define RGBANY(NAMEANY, ARGBTORGB_SIMD, ARGBTORGB_C, MASK, SBPP, BPP)          \
    void NAMEANY(const uint8* src,                                             \
                 uint8* dst,                                                   \
                 int width) {                                                  \
      int n = width & ~MASK;                                                   \
      if (n > 0) {                                                             \
        ARGBTORGB_SIMD(src, dst, n);                                           \
      }                                                                        \
      ARGBTORGB_C(src + n * SBPP, dst + n * BPP, width & MASK);                \
    }

#if defined(HAS_ARGBTORGB24ROW_SSSE3)
RGBANY(ARGBToRGB24Row_Any_SSSE3, ARGBToRGB24Row_SSSE3, ARGBToRGB24Row_C,
       15, 4, 3)
RGBANY(ARGBToRAWRow_Any_SSSE3, ARGBToRAWRow_SSSE3, ARGBToRAWRow_C,
       15, 4, 3)
RGBANY(ARGBToRGB565Row_Any_SSE2, ARGBToRGB565Row_SSE2, ARGBToRGB565Row_C,
       3, 4, 2)
RGBANY(ARGBToARGB1555Row_Any_SSE2, ARGBToARGB1555Row_SSE2, ARGBToARGB1555Row_C,
       3, 4, 2)
RGBANY(ARGBToARGB4444Row_Any_SSE2, ARGBToARGB4444Row_SSE2, ARGBToARGB4444Row_C,
       3, 4, 2)
#endif
#if defined(HAS_I400TOARGBROW_SSE2)
RGBANY(I400ToARGBRow_Any_SSE2, I400ToARGBRow_SSE2, I400ToARGBRow_C,
       7, 1, 4)
#endif
#if defined(HAS_YTOARGBROW_SSE2)
RGBANY(YToARGBRow_Any_SSE2, YToARGBRow_SSE2, YToARGBRow_C,
       7, 1, 4)
RGBANY(YUY2ToARGBRow_Any_SSSE3, YUY2ToARGBRow_SSSE3, YUY2ToARGBRow_C,
       15, 2, 4)
RGBANY(UYVYToARGBRow_Any_SSSE3, UYVYToARGBRow_SSSE3, UYVYToARGBRow_C,
       15, 2, 4)
// These require alignment on ARGB, so C is used for remainder.
RGBANY(RGB24ToARGBRow_Any_SSSE3, RGB24ToARGBRow_SSSE3, RGB24ToARGBRow_C,
       15, 3, 4)
RGBANY(RAWToARGBRow_Any_SSSE3, RAWToARGBRow_SSSE3, RAWToARGBRow_C,
       15, 3, 4)
RGBANY(RGB565ToARGBRow_Any_SSE2, RGB565ToARGBRow_SSE2, RGB565ToARGBRow_C,
       7, 2, 4)
RGBANY(ARGB1555ToARGBRow_Any_SSE2, ARGB1555ToARGBRow_SSE2, ARGB1555ToARGBRow_C,
       7, 2, 4)
RGBANY(ARGB4444ToARGBRow_Any_SSE2, ARGB4444ToARGBRow_SSE2, ARGB4444ToARGBRow_C,
       7, 2, 4)
#endif
#if defined(HAS_ARGBTORGB24ROW_NEON)
RGBANY(ARGBToRGB24Row_Any_NEON, ARGBToRGB24Row_NEON, ARGBToRGB24Row_C, 7, 4, 3)
RGBANY(ARGBToRAWRow_Any_NEON, ARGBToRAWRow_NEON, ARGBToRAWRow_C, 7, 4, 3)
RGBANY(ARGBToRGB565Row_Any_NEON, ARGBToRGB565Row_NEON, ARGBToRGB565Row_C,
       7, 4, 2)
RGBANY(ARGBToARGB1555Row_Any_NEON, ARGBToARGB1555Row_NEON, ARGBToARGB1555Row_C,
       7, 4, 2)
RGBANY(ARGBToARGB4444Row_Any_NEON, ARGBToARGB4444Row_NEON, ARGBToARGB4444Row_C,
       7, 4, 2)
RGBANY(I400ToARGBRow_Any_NEON, I400ToARGBRow_NEON, I400ToARGBRow_C,
       7, 1, 4)
RGBANY(YToARGBRow_Any_NEON, YToARGBRow_NEON, YToARGBRow_C,
       7, 1, 4)
RGBANY(YUY2ToARGBRow_Any_NEON, YUY2ToARGBRow_NEON, YUY2ToARGBRow_C,
       7, 2, 4)
RGBANY(UYVYToARGBRow_Any_NEON, UYVYToARGBRow_NEON, UYVYToARGBRow_C,
       7, 2, 4)
#endif
#undef RGBANY

// ARGB to Bayer does multiple of 4 pixels, SSSE3 aligned src, unaligned dst.
#define BAYERANY(NAMEANY, ARGBTORGB_SIMD, ARGBTORGB_C, MASK, SBPP, BPP)        \
    void NAMEANY(const uint8* src,                                             \
                 uint8* dst, uint32 selector,                                  \
                 int width) {                                                  \
      int n = width & ~MASK;                                                   \
      if (n > 0) {                                                             \
        ARGBTORGB_SIMD(src, dst, selector, n);                                 \
      }                                                                        \
      ARGBTORGB_C(src + n * SBPP, dst + n * BPP, selector, width & MASK);      \
    }

#if defined(HAS_ARGBTOBAYERROW_SSSE3)
BAYERANY(ARGBToBayerRow_Any_SSSE3, ARGBToBayerRow_SSSE3, ARGBToBayerRow_C,
         7, 4, 1)
#endif
#if defined(HAS_ARGBTOBAYERROW_NEON)
BAYERANY(ARGBToBayerRow_Any_NEON, ARGBToBayerRow_NEON, ARGBToBayerRow_C,
         7, 4, 1)
#endif
#if defined(HAS_ARGBTOBAYERGGROW_SSE2)
BAYERANY(ARGBToBayerGGRow_Any_SSE2, ARGBToBayerGGRow_SSE2, ARGBToBayerGGRow_C,
         7, 4, 1)
#endif
#if defined(HAS_ARGBTOBAYERGGROW_NEON)
BAYERANY(ARGBToBayerGGRow_Any_NEON, ARGBToBayerGGRow_NEON, ARGBToBayerGGRow_C,
         7, 4, 1)
#endif

#undef BAYERANY

#define YANY(NAMEANY, ARGBTOY_SIMD, ARGBTOY_C, SBPP, BPP, MASK)                \
    void NAMEANY(const uint8* src_argb, uint8* dst_y, int width) {             \
      int n = width & ~MASK;                                                   \
      if (n > 0) {                                                             \
        ARGBTOY_SIMD(src_argb, dst_y, n);                                      \
      }                                                                        \
      ARGBTOY_C(src_argb + n * SBPP,                                           \
                dst_y  + n * BPP, width & MASK);                               \
    }
#ifdef HAS_ARGBTOYROW_AVX2
YANY(ARGBToYRow_Any_AVX2, ARGBToYRow_AVX2, ARGBToYRow_C, 4, 1, 31)
#endif
#ifdef HAS_ARGBTOYJROW_AVX2
YANY(ARGBToYJRow_Any_AVX2, ARGBToYJRow_AVX2, ARGBToYJRow_C, 4, 1, 31)
#endif
#ifdef HAS_UYVYTOYROW_AVX2
YANY(UYVYToYRow_Any_AVX2, UYVYToYRow_AVX2, UYVYToYRow_C, 2, 1, 31)
#endif
#ifdef HAS_YUY2TOYROW_AVX2
YANY(YUY2ToYRow_Any_AVX2, YUY2ToYRow_AVX2, YUY2ToYRow_C, 2, 1, 31)
#endif
#ifdef HAS_ARGBTOYROW_SSSE3
YANY(ARGBToYRow_Any_SSSE3, ARGBToYRow_SSSE3, ARGBToYRow_C, 4, 1, 15)
#endif
#ifdef HAS_BGRATOYROW_SSSE3
YANY(BGRAToYRow_Any_SSSE3, BGRAToYRow_SSSE3, BGRAToYRow_C, 4, 1, 15)
YANY(ABGRToYRow_Any_SSSE3, ABGRToYRow_SSSE3, ABGRToYRow_C, 4, 1, 15)
YANY(RGBAToYRow_Any_SSSE3, RGBAToYRow_SSSE3, RGBAToYRow_C, 4, 1, 15)
YANY(YUY2ToYRow_Any_SSE2, YUY2ToYRow_SSE2, YUY2ToYRow_C, 2, 1, 15)
YANY(UYVYToYRow_Any_SSE2, UYVYToYRow_SSE2, UYVYToYRow_C, 2, 1, 15)
#endif
#ifdef HAS_ARGBTOYJROW_SSSE3
YANY(ARGBToYJRow_Any_SSSE3, ARGBToYJRow_SSSE3, ARGBToYJRow_C, 4, 1, 15)
#endif
#ifdef HAS_ARGBTOYROW_NEON
YANY(ARGBToYRow_Any_NEON, ARGBToYRow_NEON, ARGBToYRow_C, 4, 1, 7)
#endif
#ifdef HAS_ARGBTOYJROW_NEON
YANY(ARGBToYJRow_Any_NEON, ARGBToYJRow_NEON, ARGBToYJRow_C, 4, 1, 7)
#endif
#ifdef HAS_BGRATOYROW_NEON
YANY(BGRAToYRow_Any_NEON, BGRAToYRow_NEON, BGRAToYRow_C, 4, 1, 7)
#endif
#ifdef HAS_ABGRTOYROW_NEON
YANY(ABGRToYRow_Any_NEON, ABGRToYRow_NEON, ABGRToYRow_C, 4, 1, 7)
#endif
#ifdef HAS_RGBATOYROW_NEON
YANY(RGBAToYRow_Any_NEON, RGBAToYRow_NEON, RGBAToYRow_C, 4, 1, 7)
#endif
#ifdef HAS_RGB24TOYROW_NEON
YANY(RGB24ToYRow_Any_NEON, RGB24ToYRow_NEON, RGB24ToYRow_C, 3, 1, 7)
#endif
#ifdef HAS_RAWTOYROW_NEON
YANY(RAWToYRow_Any_NEON, RAWToYRow_NEON, RAWToYRow_C, 3, 1, 7)
#endif
#ifdef HAS_RGB565TOYROW_NEON
YANY(RGB565ToYRow_Any_NEON, RGB565ToYRow_NEON, RGB565ToYRow_C, 2, 1, 7)
#endif
#ifdef HAS_ARGB1555TOYROW_NEON
YANY(ARGB1555ToYRow_Any_NEON, ARGB1555ToYRow_NEON, ARGB1555ToYRow_C, 2, 1, 7)
#endif
#ifdef HAS_ARGB4444TOYROW_NEON
YANY(ARGB4444ToYRow_Any_NEON, ARGB4444ToYRow_NEON, ARGB4444ToYRow_C, 2, 1, 7)
#endif
#ifdef HAS_YUY2TOYROW_NEON
YANY(YUY2ToYRow_Any_NEON, YUY2ToYRow_NEON, YUY2ToYRow_C, 2, 1, 15)
#endif
#ifdef HAS_UYVYTOYROW_NEON
YANY(UYVYToYRow_Any_NEON, UYVYToYRow_NEON, UYVYToYRow_C, 2, 1, 15)
#endif
#ifdef HAS_RGB24TOARGBROW_NEON
YANY(RGB24ToARGBRow_Any_NEON, RGB24ToARGBRow_NEON, RGB24ToARGBRow_C, 3, 4, 7)
#endif
#ifdef HAS_RAWTOARGBROW_NEON
YANY(RAWToARGBRow_Any_NEON, RAWToARGBRow_NEON, RAWToARGBRow_C, 3, 4, 7)
#endif
#ifdef HAS_RGB565TOARGBROW_NEON
YANY(RGB565ToARGBRow_Any_NEON, RGB565ToARGBRow_NEON, RGB565ToARGBRow_C, 2, 4, 7)
#endif
#ifdef HAS_ARGB1555TOARGBROW_NEON
YANY(ARGB1555ToARGBRow_Any_NEON, ARGB1555ToARGBRow_NEON, ARGB1555ToARGBRow_C,
     2, 4, 7)
#endif
#ifdef HAS_ARGB4444TOARGBROW_NEON
YANY(ARGB4444ToARGBRow_Any_NEON, ARGB4444ToARGBRow_NEON, ARGB4444ToARGBRow_C,
     2, 4, 7)
#endif
#ifdef HAS_ARGBATTENUATEROW_SSSE3
YANY(ARGBAttenuateRow_Any_SSSE3, ARGBAttenuateRow_SSSE3, ARGBAttenuateRow_C,
     4, 4, 3)
#endif
#ifdef HAS_ARGBATTENUATEROW_SSE2
YANY(ARGBAttenuateRow_Any_SSE2, ARGBAttenuateRow_SSE2, ARGBAttenuateRow_C,
     4, 4, 3)
#endif
#ifdef HAS_ARGBUNATTENUATEROW_SSE2
YANY(ARGBUnattenuateRow_Any_SSE2, ARGBUnattenuateRow_SSE2, ARGBUnattenuateRow_C,
     4, 4, 3)
#endif
#ifdef HAS_ARGBATTENUATEROW_AVX2
YANY(ARGBAttenuateRow_Any_AVX2, ARGBAttenuateRow_AVX2, ARGBAttenuateRow_C,
     4, 4, 7)
#endif
#ifdef HAS_ARGBUNATTENUATEROW_AVX2
YANY(ARGBUnattenuateRow_Any_AVX2, ARGBUnattenuateRow_AVX2, ARGBUnattenuateRow_C,
     4, 4, 7)
#endif
#ifdef HAS_ARGBATTENUATEROW_NEON
YANY(ARGBAttenuateRow_Any_NEON, ARGBAttenuateRow_NEON, ARGBAttenuateRow_C,
     4, 4, 7)
#endif
#undef YANY

// RGB/YUV to UV does multiple of 16 with SIMD and remainder with C.
#define UVANY(NAMEANY, ANYTOUV_SIMD, ANYTOUV_C, BPP, MASK)                     \
    void NAMEANY(const uint8* src_argb, int src_stride_argb,                   \
                 uint8* dst_u, uint8* dst_v, int width) {                      \
      int n = width & ~MASK;                                                   \
      if (n > 0) {                                                             \
        ANYTOUV_SIMD(src_argb, src_stride_argb, dst_u, dst_v, n);              \
      }                                                                        \
      ANYTOUV_C(src_argb  + n * BPP, src_stride_argb,                          \
                dst_u + (n >> 1),                                              \
                dst_v + (n >> 1),                                              \
                width & MASK);                                                 \
    }

#ifdef HAS_ARGBTOUVROW_AVX2
UVANY(ARGBToUVRow_Any_AVX2, ARGBToUVRow_AVX2, ARGBToUVRow_C, 4, 31)
#endif
#ifdef HAS_ARGBTOUVROW_SSSE3
UVANY(ARGBToUVRow_Any_SSSE3, ARGBToUVRow_SSSE3, ARGBToUVRow_C, 4, 15)
UVANY(ARGBToUVJRow_Any_SSSE3, ARGBToUVJRow_SSSE3, ARGBToUVJRow_C, 4, 15)
UVANY(BGRAToUVRow_Any_SSSE3, BGRAToUVRow_SSSE3, BGRAToUVRow_C, 4, 15)
UVANY(ABGRToUVRow_Any_SSSE3, ABGRToUVRow_SSSE3, ABGRToUVRow_C, 4, 15)
UVANY(RGBAToUVRow_Any_SSSE3, RGBAToUVRow_SSSE3, RGBAToUVRow_C, 4, 15)
#endif
#ifdef HAS_YUY2TOUVROW_AVX2
UVANY(YUY2ToUVRow_Any_AVX2, YUY2ToUVRow_AVX2, YUY2ToUVRow_C, 2, 31)
UVANY(UYVYToUVRow_Any_AVX2, UYVYToUVRow_AVX2, UYVYToUVRow_C, 2, 31)
#endif
#ifdef HAS_YUY2TOUVROW_SSE2
UVANY(YUY2ToUVRow_Any_SSE2, YUY2ToUVRow_SSE2, YUY2ToUVRow_C, 2, 15)
UVANY(UYVYToUVRow_Any_SSE2, UYVYToUVRow_SSE2, UYVYToUVRow_C, 2, 15)
#endif
#ifdef HAS_ARGBTOUVROW_NEON
UVANY(ARGBToUVRow_Any_NEON, ARGBToUVRow_NEON, ARGBToUVRow_C, 4, 15)
#endif
#ifdef HAS_ARGBTOUVJROW_NEON
UVANY(ARGBToUVJRow_Any_NEON, ARGBToUVJRow_NEON, ARGBToUVJRow_C, 4, 15)
#endif
#ifdef HAS_BGRATOUVROW_NEON
UVANY(BGRAToUVRow_Any_NEON, BGRAToUVRow_NEON, BGRAToUVRow_C, 4, 15)
#endif
#ifdef HAS_ABGRTOUVROW_NEON
UVANY(ABGRToUVRow_Any_NEON, ABGRToUVRow_NEON, ABGRToUVRow_C, 4, 15)
#endif
#ifdef HAS_RGBATOUVROW_NEON
UVANY(RGBAToUVRow_Any_NEON, RGBAToUVRow_NEON, RGBAToUVRow_C, 4, 15)
#endif
#ifdef HAS_RGB24TOUVROW_NEON
UVANY(RGB24ToUVRow_Any_NEON, RGB24ToUVRow_NEON, RGB24ToUVRow_C, 3, 15)
#endif
#ifdef HAS_RAWTOUVROW_NEON
UVANY(RAWToUVRow_Any_NEON, RAWToUVRow_NEON, RAWToUVRow_C, 3, 15)
#endif
#ifdef HAS_RGB565TOUVROW_NEON
UVANY(RGB565ToUVRow_Any_NEON, RGB565ToUVRow_NEON, RGB565ToUVRow_C, 2, 15)
#endif
#ifdef HAS_ARGB1555TOUVROW_NEON
UVANY(ARGB1555ToUVRow_Any_NEON, ARGB1555ToUVRow_NEON, ARGB1555ToUVRow_C, 2, 15)
#endif
#ifdef HAS_ARGB4444TOUVROW_NEON
UVANY(ARGB4444ToUVRow_Any_NEON, ARGB4444ToUVRow_NEON, ARGB4444ToUVRow_C, 2, 15)
#endif
#ifdef HAS_YUY2TOUVROW_NEON
UVANY(YUY2ToUVRow_Any_NEON, YUY2ToUVRow_NEON, YUY2ToUVRow_C, 2, 15)
#endif
#ifdef HAS_UYVYTOUVROW_NEON
UVANY(UYVYToUVRow_Any_NEON, UYVYToUVRow_NEON, UYVYToUVRow_C, 2, 15)
#endif
#undef UVANY

#define UV422ANY(NAMEANY, ANYTOUV_SIMD, ANYTOUV_C, BPP, MASK, SHIFT)           \
    void NAMEANY(const uint8* src_uv,                                          \
                 uint8* dst_u, uint8* dst_v, int width) {                      \
      int n = width & ~MASK;                                                   \
      if (n > 0) {                                                             \
        ANYTOUV_SIMD(src_uv, dst_u, dst_v, n);                                 \
      }                                                                        \
      ANYTOUV_C(src_uv  + n * BPP,                                             \
                dst_u + (n >> SHIFT),                                          \
                dst_v + (n >> SHIFT),                                          \
                width & MASK);                                                 \
    }

#ifdef HAS_ARGBTOUV444ROW_SSSE3
UV422ANY(ARGBToUV444Row_Any_SSSE3, ARGBToUV444Row_SSSE3,
         ARGBToUV444Row_C, 4, 15, 0)
#endif
#ifdef HAS_YUY2TOUV422ROW_AVX2
UV422ANY(YUY2ToUV422Row_Any_AVX2, YUY2ToUV422Row_AVX2,
         YUY2ToUV422Row_C, 2, 31, 1)
UV422ANY(UYVYToUV422Row_Any_AVX2, UYVYToUV422Row_AVX2,
         UYVYToUV422Row_C, 2, 31, 1)
#endif
#ifdef HAS_ARGBTOUV422ROW_SSSE3
UV422ANY(ARGBToUV422Row_Any_SSSE3, ARGBToUV422Row_SSSE3,
         ARGBToUV422Row_C, 4, 15, 1)
#endif
#ifdef HAS_YUY2TOUV422ROW_SSE2
UV422ANY(YUY2ToUV422Row_Any_SSE2, YUY2ToUV422Row_SSE2,
         YUY2ToUV422Row_C, 2, 15, 1)
UV422ANY(UYVYToUV422Row_Any_SSE2, UYVYToUV422Row_SSE2,
         UYVYToUV422Row_C, 2, 15, 1)
#endif
#ifdef HAS_YUY2TOUV422ROW_NEON
UV422ANY(ARGBToUV444Row_Any_NEON, ARGBToUV444Row_NEON,
         ARGBToUV444Row_C, 4, 7, 0)
UV422ANY(ARGBToUV422Row_Any_NEON, ARGBToUV422Row_NEON,
         ARGBToUV422Row_C, 4, 15, 1)
UV422ANY(ARGBToUV411Row_Any_NEON, ARGBToUV411Row_NEON,
         ARGBToUV411Row_C, 4, 31, 2)
UV422ANY(YUY2ToUV422Row_Any_NEON, YUY2ToUV422Row_NEON,
         YUY2ToUV422Row_C, 2, 15, 1)
UV422ANY(UYVYToUV422Row_Any_NEON, UYVYToUV422Row_NEON,
         UYVYToUV422Row_C, 2, 15, 1)
#endif
#undef UV422ANY

#define SPLITUVROWANY(NAMEANY, ANYTOUV_SIMD, ANYTOUV_C, MASK)                  \
    void NAMEANY(const uint8* src_uv,                                          \
                 uint8* dst_u, uint8* dst_v, int width) {                      \
      int n = width & ~MASK;                                                   \
      if (n > 0) {                                                             \
        ANYTOUV_SIMD(src_uv, dst_u, dst_v, n);                                 \
      }                                                                        \
      ANYTOUV_C(src_uv + n * 2,                                                \
                dst_u + n,                                                     \
                dst_v + n,                                                     \
                width & MASK);                                                 \
    }

#ifdef HAS_SPLITUVROW_SSE2
SPLITUVROWANY(SplitUVRow_Any_SSE2, SplitUVRow_SSE2, SplitUVRow_C, 15)
#endif
#ifdef HAS_SPLITUVROW_AVX2
SPLITUVROWANY(SplitUVRow_Any_AVX2, SplitUVRow_AVX2, SplitUVRow_C, 31)
#endif
#ifdef HAS_SPLITUVROW_NEON
SPLITUVROWANY(SplitUVRow_Any_NEON, SplitUVRow_NEON, SplitUVRow_C, 15)
#endif
#ifdef HAS_SPLITUVROW_MIPS_DSPR2
SPLITUVROWANY(SplitUVRow_Any_MIPS_DSPR2, SplitUVRow_MIPS_DSPR2,
              SplitUVRow_C, 15)
#endif
#undef SPLITUVROWANY

#define MERGEUVROW_ANY(NAMEANY, ANYTOUV_SIMD, ANYTOUV_C, MASK)                 \
    void NAMEANY(const uint8* src_u, const uint8* src_v,                       \
                 uint8* dst_uv, int width) {                                   \
      int n = width & ~MASK;                                                   \
      if (n > 0) {                                                             \
        ANYTOUV_SIMD(src_u, src_v, dst_uv, n);                                 \
      }                                                                        \
      ANYTOUV_C(src_u + n,                                                     \
                src_v + n,                                                     \
                dst_uv + n * 2,                                                \
                width & MASK);                                                 \
    }

#ifdef HAS_MERGEUVROW_SSE2
MERGEUVROW_ANY(MergeUVRow_Any_SSE2, MergeUVRow_SSE2, MergeUVRow_C, 15)
#endif
#ifdef HAS_MERGEUVROW_AVX2
MERGEUVROW_ANY(MergeUVRow_Any_AVX2, MergeUVRow_AVX2, MergeUVRow_C, 31)
#endif
#ifdef HAS_MERGEUVROW_NEON
MERGEUVROW_ANY(MergeUVRow_Any_NEON, MergeUVRow_NEON, MergeUVRow_C, 15)
#endif
#undef MERGEUVROW_ANY

#define MATHROW_ANY(NAMEANY, ARGBMATH_SIMD, ARGBMATH_C, MASK)                  \
    void NAMEANY(const uint8* src_argb0, const uint8* src_argb1,               \
                 uint8* dst_argb, int width) {                                 \
      int n = width & ~MASK;                                                   \
      if (n > 0) {                                                             \
        ARGBMATH_SIMD(src_argb0, src_argb1, dst_argb, n);                      \
      }                                                                        \
      ARGBMATH_C(src_argb0 + n * 4,                                            \
                 src_argb1 + n * 4,                                            \
                 dst_argb + n * 4,                                             \
                 width & MASK);                                                \
    }

#ifdef HAS_ARGBMULTIPLYROW_SSE2
MATHROW_ANY(ARGBMultiplyRow_Any_SSE2, ARGBMultiplyRow_SSE2, ARGBMultiplyRow_C,
            3)
#endif
#ifdef HAS_ARGBADDROW_SSE2
MATHROW_ANY(ARGBAddRow_Any_SSE2, ARGBAddRow_SSE2, ARGBAddRow_C, 3)
#endif
#ifdef HAS_ARGBSUBTRACTROW_SSE2
MATHROW_ANY(ARGBSubtractRow_Any_SSE2, ARGBSubtractRow_SSE2, ARGBSubtractRow_C,
            3)
#endif
#ifdef HAS_ARGBMULTIPLYROW_AVX2
MATHROW_ANY(ARGBMultiplyRow_Any_AVX2, ARGBMultiplyRow_AVX2, ARGBMultiplyRow_C,
            7)
#endif
#ifdef HAS_ARGBADDROW_AVX2
MATHROW_ANY(ARGBAddRow_Any_AVX2, ARGBAddRow_AVX2, ARGBAddRow_C, 7)
#endif
#ifdef HAS_ARGBSUBTRACTROW_AVX2
MATHROW_ANY(ARGBSubtractRow_Any_AVX2, ARGBSubtractRow_AVX2, ARGBSubtractRow_C,
            7)
#endif
#ifdef HAS_ARGBMULTIPLYROW_NEON
MATHROW_ANY(ARGBMultiplyRow_Any_NEON, ARGBMultiplyRow_NEON, ARGBMultiplyRow_C,
            7)
#endif
#ifdef HAS_ARGBADDROW_NEON
MATHROW_ANY(ARGBAddRow_Any_NEON, ARGBAddRow_NEON, ARGBAddRow_C, 7)
#endif
#ifdef HAS_ARGBSUBTRACTROW_NEON
MATHROW_ANY(ARGBSubtractRow_Any_NEON, ARGBSubtractRow_NEON, ARGBSubtractRow_C,
            7)
#endif
#undef MATHROW_ANY

// Shuffle may want to work in place, so last16 method can not be used.
#define YANY(NAMEANY, ARGBTOY_SIMD, ARGBTOY_C, SBPP, BPP, MASK)                \
    void NAMEANY(const uint8* src_argb, uint8* dst_argb,                       \
                 const uint8* shuffler, int width) {                           \
      int n = width & ~MASK;                                                   \
      if (n > 0) {                                                             \
        ARGBTOY_SIMD(src_argb, dst_argb, shuffler, n);                         \
      }                                                                        \
      ARGBTOY_C(src_argb + n * SBPP,                                           \
                dst_argb  + n * BPP, shuffler, width & MASK);                  \
    }

#ifdef HAS_ARGBSHUFFLEROW_SSE2
YANY(ARGBShuffleRow_Any_SSE2, ARGBShuffleRow_SSE2,
     ARGBShuffleRow_C, 4, 4, 3)
#endif
#ifdef HAS_ARGBSHUFFLEROW_SSSE3
YANY(ARGBShuffleRow_Any_SSSE3, ARGBShuffleRow_SSSE3,
     ARGBShuffleRow_C, 4, 4, 7)
#endif
#ifdef HAS_ARGBSHUFFLEROW_AVX2
YANY(ARGBShuffleRow_Any_AVX2, ARGBShuffleRow_AVX2,
     ARGBShuffleRow_C, 4, 4, 15)
#endif
#ifdef HAS_ARGBSHUFFLEROW_NEON
YANY(ARGBShuffleRow_Any_NEON, ARGBShuffleRow_NEON,
     ARGBShuffleRow_C, 4, 4, 3)
#endif
#undef YANY

// Interpolate may want to work in place, so last16 method can not be used.
#define NANY(NAMEANY, TERP_SIMD, TERP_C, SBPP, BPP, MASK)                      \
    void NAMEANY(uint8* dst_ptr, const uint8* src_ptr,                         \
                 ptrdiff_t src_stride_ptr, int width,                          \
                 int source_y_fraction) {                                      \
      int n = width & ~MASK;                                                   \
      if (n > 0) {                                                             \
        TERP_SIMD(dst_ptr, src_ptr, src_stride_ptr, n, source_y_fraction);     \
      }                                                                        \
      TERP_C(dst_ptr + n * BPP,                                                \
             src_ptr + n * SBPP, src_stride_ptr,                               \
             width & MASK, source_y_fraction);                                 \
    }

#ifdef HAS_INTERPOLATEROW_AVX2
NANY(InterpolateRow_Any_AVX2, InterpolateRow_AVX2, InterpolateRow_C, 1, 1, 31)
#endif
#ifdef HAS_INTERPOLATEROW_SSSE3
NANY(InterpolateRow_Any_SSSE3, InterpolateRow_SSSE3, InterpolateRow_C, 1, 1, 15)
#endif
#ifdef HAS_INTERPOLATEROW_SSE2
NANY(InterpolateRow_Any_SSE2, InterpolateRow_SSE2, InterpolateRow_C, 1, 1, 15)
#endif
#ifdef HAS_INTERPOLATEROW_NEON
NANY(InterpolateRow_Any_NEON, InterpolateRow_NEON, InterpolateRow_C, 1, 1, 15)
#endif
#ifdef HAS_INTERPOLATEROW_MIPS_DSPR2
NANY(InterpolateRow_Any_MIPS_DSPR2, InterpolateRow_MIPS_DSPR2, InterpolateRow_C,
     1, 1, 3)
#endif
#undef NANY

#define MANY(NAMEANY, MIRROR_SIMD, MIRROR_C, BPP, MASK)                        \
    void NAMEANY(const uint8* src_y, uint8* dst_y, int width) {                \
      int n = width & ~MASK;                                                   \
      int r = width & MASK;                                                    \
      if (n > 0) {                                                             \
        MIRROR_SIMD(src_y, dst_y + r * BPP, n);                                \
      }                                                                        \
      MIRROR_C(src_y + n * BPP, dst_y, r);                                     \
    }

#ifdef HAS_MIRRORROW_AVX2
MANY(MirrorRow_Any_AVX2, MirrorRow_AVX2, MirrorRow_C, 1, 31)
#endif
#ifdef HAS_MIRRORROW_SSSE3
MANY(MirrorRow_Any_SSSE3, MirrorRow_SSSE3, MirrorRow_C, 1, 15)
#endif
#ifdef HAS_MIRRORROW_SSE2
MANY(MirrorRow_Any_SSE2, MirrorRow_SSE2, MirrorRow_C, 1, 15)
#endif
#ifdef HAS_MIRRORROW_NEON
MANY(MirrorRow_Any_NEON, MirrorRow_NEON, MirrorRow_C, 1, 15)
#endif
#ifdef HAS_ARGBMIRRORROW_AVX2
MANY(ARGBMirrorRow_Any_AVX2, ARGBMirrorRow_AVX2, ARGBMirrorRow_C, 4, 7)
#endif
#ifdef HAS_ARGBMIRRORROW_SSE2
MANY(ARGBMirrorRow_Any_SSE2, ARGBMirrorRow_SSE2, ARGBMirrorRow_C, 4, 3)
#endif
#ifdef HAS_ARGBMIRRORROW_NEON
MANY(ARGBMirrorRow_Any_NEON, ARGBMirrorRow_NEON, ARGBMirrorRow_C, 4, 3)
#endif
#undef MANY

#define MANY(NAMEANY, COPY_SIMD, COPY_C, BPP, MASK)                            \
    void NAMEANY(const uint8* src_y, uint8* dst_y, int width) {                \
      int n = width & ~MASK;                                                   \
      int r = width & MASK;                                                    \
      if (n > 0) {                                                             \
        COPY_SIMD(src_y, dst_y, n);                                            \
      }                                                                        \
      COPY_C(src_y + n * BPP, dst_y + n * BPP, r);                             \
    }

#ifdef HAS_COPYROW_AVX
MANY(CopyRow_Any_AVX, CopyRow_AVX, CopyRow_C, 1, 63)
#endif
#ifdef HAS_COPYROW_SSE2
MANY(CopyRow_Any_SSE2, CopyRow_SSE2, CopyRow_C, 1, 31)
#endif
#ifdef HAS_COPYROW_NEON
MANY(CopyRow_Any_NEON, CopyRow_NEON, CopyRow_C, 1, 31)
#endif
#undef MANY

#ifdef __cplusplus
}  // extern "C"
}  // namespace libyuv
#endif
