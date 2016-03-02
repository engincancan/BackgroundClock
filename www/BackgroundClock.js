"use strict";

var exec = require('cordova/exec');

var BackgroundClock = function() {

    var methods = [
        'init',
        'settime',
        'gettime'
    ];

    var execCall = function(methodName, args) {
        var success = log,
            fail = log;
        if (!!args && args.length && args.length > 1) {
            success = args.splice(0, 1);
            fail = args.splice(0, 1);
        }
        exec(success, fail, "BackgroundClock", methodName, Array.prototype.slice.call(args || []));
    };

    var self = this;
    for (var i = 0; i < methods.length; i++) {
        // Wrapping the callback definition call into a temporary function, otherwise i will always
        // be set to methods.length when calling execCall()
        (function(idx) {
            var currentMethod = methods[idx];
            self[currentMethod] = function() {
                execCall(currentMethod, arguments);
            };
        })(i);
    }

    function log(log) {
        console.log("log:", log);
    }
};

var BackgroundClock = new BackgroundClock();

module.exports = BackgroundClock;
