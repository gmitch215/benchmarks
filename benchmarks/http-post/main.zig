const std = @import("std");

pub fn main() !void {
    const before: i128 = std.time.milliTimestamp();

    var gpa = std.heap.GeneralPurposeAllocator(.{}){};
    defer _ = gpa.deinit();

    var client = std.http.Client{ .allocator = gpa.allocator() };
    defer client.deinit();

    var buf: [4096]u8 = undefined;

    const payload = "Hello World";

    const uri = try std.Uri.parse("http://httpbin.org/post");
    var req = try client.open(.POST, uri, .{ .server_header_buffer = &buf });
    defer req.deinit();

    req.transfer_encoding = .{ .content_length = payload.len };
    try req.send();
    var wtr = req.writer();
    try wtr.writeAll(payload);
    try req.finish();
    try req.wait();

    const after: i128 = std.time.milliTimestamp();

    std.debug.print("{}", .{after - before});
}