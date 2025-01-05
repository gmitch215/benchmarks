const std = @import("std");

const phrases: [25][]const u8 = [_][]const u8{
    "Seize the day",
    "Time flies quickly",
    "Reach for the stars",
    "Love conquers all",
    "Break the ice",
    "A blessing in disguise",
    "Actions speak louder than words",
    "Every cloud has a silver lining",
    "Burn the midnight oil",
    "Jack of all trades",
    "Piece of cake",
    "Bite the bullet",
    "Crystal clear",
    "Better late than never",
    "The early bird catches the worm",
    "Cat got your tongue",
    "A penny for your thoughts",
    "Laughter is the best medicine",
    "All ears",
    "Barking up the wrong tree",
    "Beauty is in the eye of the beholder",
    "The calm before the storm",
    "Let the cat out",
    "On thin ice",
    "Cut to the chase"
};

pub fn main() !void {
    const before: i128 = std.time.nanoTimestamp();

    var sha1 = std.crypto.hash.sha2.Sha256.init(.{});

    for (phrases) |phrase| {
        var out: [32]u8 = undefined;
        sha1.update(phrase);
        sha1.final(&out);
    }

    const after: i128 = std.time.nanoTimestamp();

    std.debug.print("{}", .{after - before});
}