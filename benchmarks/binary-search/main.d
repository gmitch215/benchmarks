#!/usr/bin/env dub
/+dub.sdl:
targetName "main.d.o"
+/
import std.stdio : writeln;
import std.datetime.stopwatch : StopWatch, AutoStart;

int binarySearch(int[] arr, int target)
{
    int left = 0;
    int right = cast(int) arr.length - 1;

    while (left <= right)
    {
        int mid = left + (right - left) / 2;
        if (arr[mid] == target)
            return mid;
        if (arr[mid] < target)
            left = mid + 1;
        else
            right = mid - 1;
    }
    return -1;
}

void main()
{
    int[25] arr = [
        2, 3, 6, 10, 23, 45, 78, 129, 213, 294, 299, 301, 332, 423, 521, 543, 571,
        612, 634, 678, 712, 745, 789, 812, 834
    ];
    auto sw = StopWatch(AutoStart.yes);

    binarySearch(arr, arr[2]);
    binarySearch(arr, arr[8]);
    binarySearch(arr, arr[12]);
    binarySearch(arr, arr[15]);
    binarySearch(arr, arr[20]);

    sw.stop();
    writeln(sw.peek.total!"nsecs");
}
