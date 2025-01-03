const std = @import("std");

fn is_prime(n: f64) bool {
    var start: f64 = 2;
    const limit = std.math.sqrt(n);

    while (start <= limit) {
        if (@mod(n, start) == 0) {
            return false;
        }
        start += 1;
    }

    return true;
}

pub fn main() !void {
    const before: i128 = std.time.nanoTimestamp();

    for (0..99999) |i| {
        _ = is_prime(@as(f64, @floatFromInt(i)));
    }

    const after: i128 = std.time.nanoTimestamp();

    std.debug.print("{}", .{after - before});
}