package com.genedata.questions.javascript

import com.genedata.questions.Question

/**
 * @author Daniel Nesbitt
 */
class JSExpressions : Question {

    override fun title(): String = "JavaScript: Oh JavaScript..."

    override fun text(): String = """
        ```javascript
        var counter = 0;

        if (typeof null === 'object') {
            counter++;
        }

        if (null instanceof Object) {
            counter++;
        }

        var text = 'start';
        (function () {
            if (typeof text === 'string') {
                var text = 'finish';
                counter++;
            } else {
                counter += 2;
            }
        })();

        var array = [0, 1, 2];
        array[9] = 10;
        var result = array.filter((x) => x === undefined);
        counter += result.length;

        var x = "hello";
        if (new String(x) !== x) {
            counter++;
        }

        function isOdd(num) {
            return num % 2 === 1;
        }
        function isEven(num) {
            return num % 2 === 0;
        }
        function isSane(num) {
            return isEven(num) || isOdd(num);
        }
        var values = [7, 4, '13', -9, Infinity];
        values.forEach(x => {
            if (isOdd(x)) counter++;
        });
        values.forEach(x => {
            if (isEven(x)) counter += 2;
        });
        values.forEach(x => {
            if (isSane(x)) counter += 3;
        });
        
        counter += '1' + 4
        counter += '1' - 4
        ```
        What is value of counter once this code is executed?
    """.trimIndent()

    override fun validateAnswer(answer: String): Boolean = answer == "1714-3"

}
