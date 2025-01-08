function jumpSearch(arr, x) { 
    const n = arr.length;
    let step = Math.sqrt(n); 
    let prev = 0; 
    
    for (let minStep = Math.min(step, n)-1; arr[minStep] < x; minStep = Math.min(step, n) - 1) { 
        prev = step; 
        step += Math.sqrt(n); 
        if (prev >= n) 
            return -1;
    } 
   
    while (arr[prev] < x) { 
        prev++; 
   
        if (prev == Math.min(step, n)) 
            return -1; 
    } 
    
    if (arr[prev] == x) 
        return prev; 
   
    return -1; 
}

let arr = Array(
    2, 3, 6, 10, 23, 45, 78, 129, 213, 294, 299, 301, 332, 423, 521, 543, 571, 612, 634, 678, 712, 745, 789, 812, 834
)

let before = process.hrtime.bigint()

jumpSearch(arr, arr[2])
jumpSearch(arr, arr[8])
jumpSearch(arr, arr[12])
jumpSearch(arr, arr[15])
jumpSearch(arr, arr[20])

let after = process.hrtime.bigint()

console.log((after - before).toString())