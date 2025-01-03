const std = @import("std");

fn binary_search(arr: [25]i32, target: i32) i32 {
    var left: i32 = 0;
    var right: i32 = arr.len - 1;
    while (left <= right) {
        const mid: i32 = left + @divFloor(right - left, 2);
        if (arr[@intCast(mid)] == target) {
            return mid;
        } else if (arr[@intCast(mid)] < target) {
            left = mid + 1;
        } else {
            right = mid - 1;
        }
    }
    return -1;
}

pub fn main() !void {
    const before: i128 = std.time.nanoTimestamp();

    const arr: [25]i32 = .{2, 3, 6, 10, 23, 45, 78, 129, 213, 294, 299, 301, 332, 423, 521, 543, 571, 612, 634, 678, 712, 745, 789, 812, 834};

    _ = binary_search(arr, arr[2]);
    _ = binary_search(arr, arr[8]);
    _ = binary_search(arr, arr[12]);
    _ = binary_search(arr, arr[15]);
    _ = binary_search(arr, arr[20]);

    const after: i128 = std.time.nanoTimestamp();

    std.debug.print("{}", .{after - before});
}