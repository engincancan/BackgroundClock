"use strict";

var exec = require('cordova/exec');

var BackgroundClock = function() {

    var methods = [
        'init',
        'gettime',
        'finish',
        'getstatus',
        'updatetime'
    ];

    var execCall = function(methodName, args) {
        var success = log,
            fail = log;
        var argsArray = [].slice.call(args);
        if (!!argsArray && argsArray.length && argsArray.length > 2) {
            success = argsArray.splice(0, 1)[0];
            fail = argsArray.splice(0, 1)[0];
        }
        exec(success, fail, "BackgroundClockPlugin", methodName, argsArray || []);
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
