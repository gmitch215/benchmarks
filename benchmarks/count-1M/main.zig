const std = @import("std");

pub fn main() !void {
    const before: i128 = std.time.nanoTimestamp();

    var count: u32 = 0;
    for (1..1_000_000) |_| {
        count += 1;
    }

    const after: i128 = std.time.nanoTimestamp();

    std.debug.print("{}", .{after - before});
}