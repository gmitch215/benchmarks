use std::time::{SystemTime};

fn linear_search(arr: [i32; 50], target: i32) -> i32 {
    for i in 0..arr.len() {
        if arr[i] == target {
            return i as i32;
        }
    }
    return -1;
}

fn main() {
    let arr: [i32; 50] = [1722, 1214, 2169, 1505, 1089, 696, 1706, 1088, 1637, 250, 430, 2466, 1319, 1933, 1363, 495, 1903, 1750, 2089, 1496, 20, 2262, 483, 1054, 226, 2357, 2162, 482, 61, 675, 2003, 1914, 1687, 738, 1340, 969, 1683, 1765, 23, 317, 657, 995, 1112, 2248, 820, 2076, 56, 745, 734, 1884];

    let before = SystemTime::now().duration_since(SystemTime::UNIX_EPOCH).unwrap();

    linear_search(arr, arr[4]);
    linear_search(arr, arr[11]);
    linear_search(arr, arr[29]);
    linear_search(arr, arr[36]);
    linear_search(arr, arr[42]);

    let after = SystemTime::now().duration_since(SystemTime::UNIX_EPOCH).unwrap();
    println!("{}", after.as_nanos() - before.as_nanos());
}