package com.genedata.questions.javascript

import com.genedata.questions.Question

/**
 * @author Alice Li
 */
class JSPromises : Question {
    override fun title(): String = "JavaScript: Promises"

    override fun text(): String = """
        ```javascript
        var counter = 0;

        var a = new Promise((resolve, reject) => {
            setTimeout(() => {
                counter++;
                resolve();
            }, 1000);
        });

        var b = new Promise((resolve, reject) => {
            setTimeout(() => {
                counter += 2;
                reject();
            }, 2000);
        });

        var c = new Promise((resolve, reject) => {
            setTimeout(() => {
                counter += 3;
                reject();
            }, 1000);
        });

        var d = new Promise((resolve, reject) => {
            setTimeout(() => {
                counter += 4;
                resolve();
            }, 2000);
        });

        var foo = Promise.race([a, b]).then(() => {
            if (counter % 2) {
                counter += 5;
            } else {
                counter += 6;
                throw "Error";
            }
        }, () => {
            counter += 7
        }).catch(() => {
            counter += 8;
        }).then(() => {
            return Promise.all([c, d])
        }).then(() => {
            if (!counter % 2) {
                counter += 9;
            } else {
                counter += 10;
                throw "Error";
            }
        }, () => {
            counter += 11
        }).catch(() => {
            counter += 12;
        }).then(() => {
            console.log(counter);
        })

        ```
        What value gets printed to the console in line 56?
    """.trimIndent()

    override fun validateAnswer(answer: String): Boolean = answer == "20"
}
