// NOTICE!! DO NOT USE ANY OF THIS JAVASCRIPT
// IT'S JUST JUNK FOR OUR DOCS!
// ++++++++++++++++++++++++++++++++++++++++++
/*!
 * Copyright 2014-2015 Twitter, Inc.
 *
 * Licensed under the Creative Commons Attribution 3.0 Unported License. For
 * details, see https://creativecommons.org/licenses/by/3.0/.
 */
// Intended to prevent false-positive bug reports about Bootstrap not working properly in old versions of IE due to folks testing using IE's unreliable emulation modes.
(function () {
  'use strict';

  function emulatedIEMajorVersion() {
    let groups = /MSIE ([0-9.]+)/.exec(window.navigator.userAgent)
    if (groups === null) {
      return null
    }
    let ieVersionNum = parseInt(groups[1], 10)
    return Math.floor(ieVersionNum)
  }

  function actualNonEmulatedIEMajorVersion() {
    // Detects the actual version of IE in use, even if it's in an older-IE emulation mode.
    // IE JavaScript conditional compilation docs: https://msdn.microsoft.com/library/121hztk3%28v=vs.94%29.aspx
    // @cc_on docs: https://msdn.microsoft.com/library/8ka90k2e%28v=vs.94%29.aspx
    let jscriptVersion = new Function('/*@cc_on return @_jscript_version; @*/')() // jshint ignore:line
    if (jscriptVersion === undefined) {
      return 11 // IE11+ not in emulation mode
    }
    if (jscriptVersion < 9) {
      return 8 // IE8 (or lower; haven't tested on IE<8)
    }
    return jscriptVersion // IE9 or IE10 in any mode, or IE11 in non-IE11 mode
  }

  let ua = window.navigator.userAgent
  if (ua.indexOf('Opera') > -1 || ua.indexOf('Presto') > -1) {
    return // Opera, which might pretend to be IE
  }
  let emulated = emulatedIEMajorVersion()
  if (emulated === null) {
    return // Not IE
  }
  let nonEmulated = actualNonEmulatedIEMajorVersion()

  if (emulated !== nonEmulated) {
    window.alert('WARNING: You appear to be using IE' + nonEmulated + ' in IE' + emulated + ' emulation mode.\nIE emulation modes can behave significantly differently from ACTUAL older versions of IE.\nPLEASE DON\'T FILE BOOTSTRAP BUGS based on testing in IE emulation modes!')
  }
})();
