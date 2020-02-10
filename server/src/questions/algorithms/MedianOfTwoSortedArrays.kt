package com.genedata.questions.algorithms

import com.genedata.questions.Question

/**
 * @author Daniel Nesbitt
 */
class MedianOfTwoSortedArrays: Question {

    override fun title(): String {
        return "Algorithms: Arrays"
    }

    override fun text(): String {
        return """
            You might have an easier time with this problem if you determine the intent of the algorithm before trying to solve it
            based on the input.
            ```python
            def find_x(A, B):
                m, n = len(A), len(B)
                if m > n:
                    A, B, m, n = B, A, n, m
                if n == 0:
                    raise ValueError

                imin, imax, half_len = 0, m, (m + n + 1) / 2
                while imin <= imax:
                    i = (imin + imax) / 2
                    j = half_len - i
                    if i < m and B[j-1] > A[i]:
                        # i is too small, must increase it
                        imin = i + 1
                    elif i > 0 and A[i-1] > B[j]:
                        # i is too big, must decrease it
                        imax = i - 1
                    else:
                        # i is perfect

                        if i == 0: max_of_left = B[j-1]
                        elif j == 0: max_of_left = A[i-1]
                        else: max_of_left = max(A[i-1], B[j-1])

                        if (m + n) % 2 == 1:
                            return max_of_left

                        if i == m: min_of_right = B[j]
                        elif j == n: min_of_right = A[i]
                        else: min_of_right = min(A[i], B[j])

                        return (max_of_left + min_of_right) / 2.0
            ```

            **Inputs**
            ```python
            A=[11, 34, 71, 74, 122, 153, 154, 167, 200, 210, 228, 238, 276, 293, 314, 329, 344, 350, 360, 391, 440, 441, 523, 532, 571, 575, 591, 621, 646, 655, 661, 696, 727, 769, 785, 796, 843, 846, 858, 862, 862, 884, 896, 898, 900, 907, 915, 922, 952, 987]
            # len(A) = 50
            B=[93, 96, 168, 180, 262, 272, 273, 294, 296, 373, 382, 407, 411, 434, 439, 510, 551, 568, 579, 601, 607, 644, 739, 768, 862, 869, 889, 893, 921, 979]
            # len(B) = 30
            ```
        """.trimIndent()
    }

    override fun validateAnswer(answer: String): Boolean {
        return answer.toDouble() == 541.5;
    }

}
