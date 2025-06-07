#!/usr/bin/env dub
/+dub.sdl:
targetName "main.d.o"
+/
import std.stdio : writeln;
import std.datetime.stopwatch : StopWatch, AutoStart;
import std.net.curl : get;

void main()
{
    auto sw = StopWatch(AutoStart.yes);

    get("http://httpbin.org/get");

    sw.stop();
    writeln(sw.peek.total!"msecs");
}
