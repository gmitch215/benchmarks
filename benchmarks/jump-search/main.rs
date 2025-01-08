use std::time::{SystemTime};

fn jump_search(arr: [i32; 25], target: i32) -> i32 {
    let n = arr.len();
    if n == 0 {
        return -1;
    }

    let block_size = (n as f64).sqrt() as usize;
    let mut prev = 0;
    let mut curr = block_size;

    while prev < n && arr[std::cmp::min(curr, n) - 1] < target {
        prev = curr;
        curr += block_size;
        if prev >= n {
            return -1;
        }
    }

    for i in prev..std::cmp::min(curr, n) {
        if arr[i] == target {
            return i as i32;
        }
    }

    -1
}

fn main() {
    let arr: [i32; 25] = [2, 3, 6, 10, 23, 45, 78, 129, 213, 294, 299, 301, 332, 423, 521, 543, 571, 612, 634, 678, 712, 745, 789, 812, 834];

    let before = SystemTime::now().duration_since(SystemTime::UNIX_EPOCH).unwrap();

    jump_search(arr, arr[2]);
    jump_search(arr, arr[8]);
    jump_search(arr, arr[12]);
    jump_search(arr, arr[15]);
    jump_search(arr, arr[20]);

    let after = SystemTime::now().duration_since(SystemTime::UNIX_EPOCH).unwrap();
    println!("{}", after.as_nanos() - before.as_nanos());
}