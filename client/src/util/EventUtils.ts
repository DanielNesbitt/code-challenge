import {FormEvent, KeyboardEvent} from "react";

export type InputHandler =
    (e: FormEvent<HTMLInputElement | HTMLTextAreaElement>, v: string | undefined)
        => void;

export function fieldInput(handler: (v: string) => void, defaultValue: string = ''): InputHandler {
    return (e, v) => handler(v ?? defaultValue)
}

export type KeyboardHandler = (e: KeyboardEvent) => void

export function onEnter(handler: () => void): KeyboardHandler {
    return (e) => {
        if (e.keyCode === 13) {
            // noinspection JSIgnoredPromiseFromCall
            handler()
        }
    }
}
