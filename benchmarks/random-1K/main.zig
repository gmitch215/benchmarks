const std = @import("std");

pub fn main() !void {
    const before: i128 = std.time.nanoTimestamp();

    const rand = std.crypto.random;
    var n: i32 = 0;
    for (0..999) |_| {
        n = rand.int(i32);
    }

    const after: i128 = std.time.nanoTimestamp();

    std.debug.print("{}", .{after - before});
}