const std = @import("std");

fn jump_search(arr: [25]i32, target: i32) ?usize {
    const n = arr.len;
    if (n == 0) return null;

    const size: usize = @intFromFloat(std.math.sqrt(@as(f32, @floatFromInt(n))));
    var prev: usize = 0;

    while (prev < n and arr[if (prev + size < n) prev + size else n - 1] < target) {
        prev += size;
        if (prev >= n) return null;
    }

    var i = prev;
    while (i < if (prev + size < n) prev + size else n) : (i += 1) {
        if (arr[i] == target) return i;
    }

    return null;
}

pub fn main() !void {
    const before: i128 = std.time.nanoTimestamp();

    const arr: [25]i32 = .{2, 3, 6, 10, 23, 45, 78, 129, 213, 294, 299, 301, 332, 423, 521, 543, 571, 612, 634, 678, 712, 745, 789, 812, 834};

    _ = jump_search(arr, arr[2]);
    _ = jump_search(arr, arr[8]);
    _ = jump_search(arr, arr[12]);
    _ = jump_search(arr, arr[15]);
    _ = jump_search(arr, arr[20]);

    const after: i128 = std.time.nanoTimestamp();

    std.debug.print("{}", .{after - before});
}