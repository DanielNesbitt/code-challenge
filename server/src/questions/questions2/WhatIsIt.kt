package com.genedata.questions.questions2

import com.genedata.questions.Question

/**
 * @author Alice Li
 */
class WhatIsIt : Question {
    override fun title(): String {
        return "Python"
    }

    override fun text(): String {
        return """
``` python
def start(input):
    return foo(input, 0, len(input))

def foo(input, start, stop):
    a = (start + stop) // 2
    b = input[a]
    c = -1
    d = -1
    for i, item in enumerate(b):
        if item > c:
            c = item
            d = i
    if a > 0 and input[a - 1][d] > input[a][d]:
        return foo(input, start, a)
    elif a < len(input) - 1 and input[a + 1][d] > input[a][d]:
        return foo(input, a, stop)
    else:
        return [a, d] 
```

``` python
a = [
[73, 68, 54, 96, 12, 36, 15, 87, 34, 79],
[23, 89, 29, 91, 33, 11, 95, 65, 53, 44],
[13, 88, 55, 76, 46, 97, 98, 56,  5, 74],
[ 3, 47, 10,  7, 77, 50, 25, 38, 37,  9],
[84, 41, 93, 42, 61, 32, 19, 81, 63, 14],
[26,  4, 80, 40, 57, 20, 66,  6, 52, 69],
[39, 18, 48, 62, 71, 78, 51, 45, 49, 43],
[67, 94, 99, 22, 64, 60, 90, 86, 28, 59],
[16, 31, 35, 100, 92, 72, 70, 82,  2, 75],
[24, 83,  1, 85,  8, 21, 58, 17, 30, 27]
    ]
```
What is the result of start(a)?
        """.trimIndent()
    }

    override fun validateAnswer(answer: String): Boolean {
        return answer.trim().equals("[2, 6]", false)
    }
}
