#!/usr/bin/env dub
/+dub.sdl:
targetName "main.d.o"
+/
import std.stdio : writeln;
import std.base64 : Base64;
import std.datetime.stopwatch : StopWatch, AutoStart;

void main()
{
    enum i1 = "SGVsbG8sIFdvcmxkIQ==";
    enum i2 = "VGhlIHF1aWNrIGJyb3duIGZveCBqdW1wcyBvdmVyIHRoZSBsYXp5IGRvZy4=";
    enum i3 = "QWFCYkNjRGRFZUZmMTIzNDU2XyFAIyYqXigpJD0rLQ==";

    auto sw = StopWatch(AutoStart.yes);

    Base64.decode(i1);
    Base64.decode(i2);
    Base64.decode(i3);

    sw.stop();
    writeln(sw.peek.total!"nsecs");
}
