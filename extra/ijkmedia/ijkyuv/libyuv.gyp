# Copyright 2011 The LibYuv Project Authors. All rights reserved.
#
# Use of this source code is governed by a BSD-style license
# that can be found in the LICENSE file in the root of the source
# tree. An additional intellectual property rights grant can be found
# in the file PATENTS. All contributing project authors may
# be found in the AUTHORS file in the root of the source tree.

{
  'includes': [
    'libyuv.gypi',
  ],
  # Make sure that if we are being compiled to an xcodeproj, nothing tries to
  # include a .pch.
  'xcode_settings': {
    'GCC_PREFIX_HEADER': '',
    'GCC_PRECOMPILE_PREFIX_HEADER': 'NO',
  },
  'variables': {
    'use_system_libjpeg%': 0,
    'libyuv_disable_jpeg%': 0,
    # Link-Time Optimizations.
    'use_lto%': 0,
    'build_neon': 0,
    'conditions': [
       ['(target_arch == "armv7" or target_arch == "armv7s" or \
       (target_arch == "arm" and arm_version >= 7) or target_arch == "arm64")\
       and (arm_neon == 1 or arm_neon_optional == 1)',
       {
         'build_neon': 1,
       }],
    ],
  },
  'conditions': [
    ['build_neon != 0', {
      'targets': [
        # The NEON-specific components.
        {
          'target_name': 'libyuv_neon',
          'type': 'static_library',
          'standalone_static_library': 1,
          # TODO(noahric): This should remove whatever mfpu is set, not
          # just vfpv3-d16.
          'cflags!': [
            '-mfpu=vfp',
            '-mfpu=vfpv3',
            '-mfpu=vfpv3-d16',
          ],
          'conditions': [
            # Disable LTO in libyuv_neon target due to gcc 4.9 compiler bug.
            ['use_lto == 1', {
              'cflags!': [
                '-flto',
                '-ffat-lto-objects',
              ],
            }],
            # arm64 does not need -mfpu=neon option as neon is not optional
            ['target_arch != "arm64"', {
              'cflags': [
                '-mfpu=neon',
              ],
            }],
          ],
          'include_dirs': [
            'include',
            '.',
          ],
          'direct_dependent_settings': {
            'include_dirs': [
              'include',
              '.',
            ],
          },
          'sources': [
            # sources.
            'source/compare_neon.cc',
            'source/compare_neon64.cc',
            'source/rotate_neon.cc',
            'source/rotate_neon64.cc',
            'source/row_neon.cc',
            'source/row_neon64.cc',
            'source/scale_neon.cc',
            'source/scale_neon64.cc',
          ],
        },
      ],
    }],
  ],
  'targets': [
    {
      'target_name': 'libyuv',
      # Change type to 'shared_library' to build .so or .dll files.
      'type': 'static_library',
      # Allows libyuv.a redistributable library without external dependencies.
      'standalone_static_library': 1,
      'conditions': [
        ['OS != "ios" and libyuv_disable_jpeg != 1', {
          'defines': [
            'HAVE_JPEG'
          ],
          'conditions': [
            # Caveat system jpeg support may not support motion jpeg
            [ 'use_system_libjpeg == 1', {
              'dependencies': [
                 '<(DEPTH)/third_party/libjpeg/libjpeg.gyp:libjpeg',
              ],
            }, {
              'dependencies': [
                 '<(DEPTH)/third_party/libjpeg_turbo/libjpeg.gyp:libjpeg',
              ],
            }],
            [ 'use_system_libjpeg == 1', {
              'link_settings': {
                'libraries': [
                  '-ljpeg',
                ],
              }
            }],
          ],
        }],
        ['build_neon != 0', {
          'dependencies': [
            'libyuv_neon',
          ],
        }],
        # MemorySanitizer does not support assembly code yet.
        # http://crbug.com/344505
        [ 'msan == 1', {
          'defines': [
            'LIBYUV_DISABLE_X86',
          ],
        }],
      ], #conditions
      'defines': [
        # Enable the following 3 macros to turn off assembly for specified CPU.
        # 'LIBYUV_DISABLE_X86',
        # 'LIBYUV_DISABLE_NEON',
        # 'LIBYUV_DISABLE_MIPS',
        # Enable the following macro to build libyuv as a shared library (dll).
        # 'LIBYUV_USING_SHARED_LIBRARY',
	# TODO(fbarchard): Make these into gyp defines.
      ],
      'include_dirs': [
        'include',
        '.',
      ],
      'direct_dependent_settings': {
        'include_dirs': [
          'include',
          '.',
        ],
        'conditions': [
          ['OS == "android" and target_arch == "arm64"', {
            'ldflags': [
              '-Wl,--dynamic-linker,/system/bin/linker64',
            ],
          }],
          ['OS == "android" and target_arch != "arm64"', {
            'ldflags': [
              '-Wl,--dynamic-linker,/system/bin/linker',
            ],
          }],
        ], #conditions
      },
      'sources': [
        '<@(libyuv_sources)',
      ],
    },
  ], # targets.
}

# Local Variables:
# tab-width:2
# indent-tabs-mode:nil
# End:
# vim: set expandtab tabstop=2 shiftwidth=2:
