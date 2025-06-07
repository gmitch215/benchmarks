#!/usr/bin/env dub
/+dub.sdl:
targetName "main.d.o"
+/
import std.stdio : writeln;
import std.datetime.stopwatch : StopWatch, AutoStart;
import std.net.curl : post;

void main()
{
    auto sw = StopWatch(AutoStart.yes);

    auto body = "Hello World";
    post("http://httpbin.org/post", body);

    sw.stop();
    writeln(sw.peek.total!"msecs");
}
