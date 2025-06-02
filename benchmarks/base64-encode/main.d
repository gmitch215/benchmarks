#!/usr/bin/env dub
/+dub.sdl:
targetName "main.d.o"
+/
import std.stdio : writeln;
import std.base64 : Base64;
import std.datetime.stopwatch : StopWatch, AutoStart;

void main()
{
    enum i1 = "Hello, World!";
    enum i2 = "The quick brown fox jumps over the lazy dog.";
    enum i3 = "AaBbCcDdEeFf123456_!@#&*^()$=+-";

    auto sw = StopWatch(AutoStart.yes);

    Base64.encode(cast(ubyte[]) i1);
    Base64.encode(cast(ubyte[]) i2);
    Base64.encode(cast(ubyte[]) i3);

    sw.stop();
    writeln(sw.peek.total!"nsecs");
}
