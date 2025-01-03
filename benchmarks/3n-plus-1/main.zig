const std = @import("std");

fn next(n: u32) u32 {
    if (n % 2 == 0) {
        return n / 2;
    } else {
        return 3 * n + 1;
    }
}

pub fn main() !void {
    const before: i128 = std.time.nanoTimestamp();

    var n: u32 = 837799;
    while (n != 1) {
        n = next(n);
    }

    const after: i128 = std.time.nanoTimestamp();

    std.debug.print("{}", .{after - before});
}