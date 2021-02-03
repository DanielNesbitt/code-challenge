package com.genedata.questions.questions2

import com.genedata.questions.Question

/**
 * @author Alice Li
 */
class CSSSelectors: Question {
    override fun title(): String {
        return "CSS";
    }

    override fun text(): String {
        return """
            ```html
           <!DOCTYPE html>
            <html>
            <head>
            <style>
            .foo {
                font-size: 5px;
            }
            .bar {
                font-size: 10px;
            }
            .foo.bar {
                font-size: 15px;
            }
            .foo .bar {
                font-size: 20px;
            }
            .foo.bar.baz {
                font-size: 25px;
            }
            .baz {
                font-size: 30px;
            }
            p.baz {
                font-size: 25px;
            }
            p baz {
                font-size: 25px;
            }
            div, p {
                font-size: 35px;
            }
            div li {
                font-size: 40px;
            }
            div > li {
                font-size: 45px;
            }
            div p {
                font-size: 50px;
            }
            div > p {
                font-size: 55px;
            }
            li + li {
                font-size: 60px;
            }
            ui::after {
                font-size: 65px;
                content: " foo bar baz";
            }
            ui::first-child {
                font-size: 70px;
            }
            ul li {
                font-size:80px;
            }
            
            </style>
            </head>
            <body>
            
            <div class="foo">
                <div class="bar">
                    <p class="foo bar">foo bar baz</p>
                    <p>
                    <ul class="bar baz">
                        <li id="one">foo</li>
                        <li>bar</li>
                        <li>baz</li>
                    </ul>
                    </p>
                </div>
                <ul class="baz">
                    <li class="foo bar baz">foo</li>
                    <li class="bar baz" id="two">bar</li>
                    <li class="baz">baz</li>
                </ul>
                <ul class="baz">
                    <li><p class="foo bar baz">foo</p></li>
                    <li><p class="bar">bar</p></li>
                    <li><p class="baz" id="three">baz</p></li>
                </ul>
                <span>
                    <p class="baz">foo bar baz</p>
                </span>
                <p class="baz" id="four">foo bar baz</p>
            </div>
            
            </body>
            </html>
            ```
            ```javascript
            var one = document.querySelector("#one");
            var answer1 = window.getComputedStyle(one, null).getPropertyValue("font-size");
            var two = document.querySelector("#two");
            var answer2 = window.getComputedStyle(two, null).getPropertyValue("font-size");
            var three = document.querySelector("#three");
            var answer3 = window.getComputedStyle(three, null).getPropertyValue("font-size");
            var four = document.querySelector("#four");
            var answer4 = window.getComputedStyle(four, null).getPropertyValue("font-size");
            ```
            What is answer1 + ' ' + answer2 + ' ' + answer3 + ' ' + answer4?
            
        """.trimIndent()
    }

    override fun validateAnswer(answer: String): Boolean {
        return answer == "80px 20px 25px 25px";
    }
}
