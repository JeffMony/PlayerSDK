# Copyright 2014 The LibYuv Project Authors. All rights reserved.
#
# Use of this source code is governed by a BSD-style license
# that can be found in the LICENSE file in the root of the source
# tree. An additional intellectual property rights grant can be found
# in the file PATENTS. All contributing project authors may
# be found in the AUTHORS file in the root of the source tree.

import re
import sys


def GetDefaultTryConfigs(bots=None):
  """Returns a list of ('bot', set(['tests']), optionally filtered by [bots].

  For WebRTC purposes, we always return an empty list of tests, since we want
  to run all tests by default on all our trybots.
  """
  return { 'tryserver.libyuv': dict((bot, []) for bot in bots)}


# pylint: disable=W0613
def GetPreferredTryMasters(project, change):
  files = change.LocalPaths()
  bots = [
    'win',
    'win_rel',
    'win_x64_rel',
    'mac',
    'mac_rel',
    'mac_x64_rel',
    'ios',
    'ios_rel',
    'ios_arm64',
    'ios_arm64_rel',
    'mac_asan',
    'mac_xcode61',
    'mac_rel_xcode61',
    'mac_x64_rel_xcode61',
    'ios_xcode61',
    'ios_rel_xcode61',
    'ios_arm64_xcode61',
    'ios_arm64_rel_xcode61',
    'mac_asan_xcode61',
    'linux',
    'linux_rel',
    'linux_memcheck',
    'linux_tsan2',
    'linux_asan',
    'android',
    'android_rel',
  ]
  if not files or all(re.search(r'[\\/]OWNERS$', f) for f in files):
    return {}
  return GetDefaultTryConfigs(bots)
