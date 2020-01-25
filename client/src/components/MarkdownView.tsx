import React from "react";
import ReactMarkdown from "react-markdown";
import SyntaxHighlighter from 'react-syntax-highlighter';
import {tomorrow} from 'react-syntax-highlighter/dist/esm/styles/hljs';

type CodeHighlighterProps = {
    value: string,
    language?: string
}
const CodeHighlighter: React.FC<CodeHighlighterProps> = ({value, language}) => (
    <SyntaxHighlighter language={language} style={tomorrow} showLineNumbers>{value}</SyntaxHighlighter>
);

export type MarkdownViewProps = { markdown: string }

export const MarkdownView: React.FC<MarkdownViewProps> = ({markdown}) => (
    <ReactMarkdown source={markdown} renderers={{code: CodeHighlighter}}/>
);
