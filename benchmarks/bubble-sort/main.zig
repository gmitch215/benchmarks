const std = @import("std");

fn bubble_sort(arr: []i32) void {
    var n = arr.len;
    var swapped = true;
    while (swapped) {
        swapped = false;
        for (1..n) |i| {
            if (arr[i - 1] > arr[i]) {
                const temp = arr[i - 1];
                arr[i - 1] = arr[i];
                arr[i] = temp;
                swapped = true;
            }
        }
        n -= 1;
    }
}

pub fn main() !void {
    const before: i128 = std.time.nanoTimestamp();

    var arr = [_]i32{639, 575, 652, 103, 262, 571, 542, 11, 278, 144, 885, 300, 897, 303, 726, 272, 214, 346, 935, 409, 76, 877, 367, 837, 728, 761, 205, 143, 658, 908, 263, 388, 317, 159, 331, 107, 127, 635, 972, 773, 81, 964, 302, 294, 82, 7, 671, 507, 802, 966};
    bubble_sort(&arr);

    const after: i128 = std.time.nanoTimestamp();

    std.debug.print("{}", .{after - before});
}