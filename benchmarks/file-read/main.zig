const std = @import("std");

pub fn main() !void {
    const before: i128 = std.time.nanoTimestamp();

    var file = try std.fs.cwd().openFile("file.txt", .{});
    defer file.close();

    var reader = std.io.bufferedReader(file.reader());
    var stream = reader.reader();

    var buffer: [1024]u8 = undefined;

    while (try stream.readUntilDelimiterOrEof(&buffer, '\n')) |_| {
        // Do Nothing
    }

    const after: i128 = std.time.nanoTimestamp();

    std.debug.print("{}", .{after - before});
}