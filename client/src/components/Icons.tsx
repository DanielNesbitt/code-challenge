import React from "react";
import {Icon, IStyle} from "office-ui-fabric-react";

const defaultStyles: Partial<IStyle> = {
    marginBottom: -3,
    fontSize: 18,
    fontWeight: "bold"
};

export const goodAnswer = <Icon iconName={"Checkmark"} styles={{root: {...defaultStyles, color: "green"}}}/>;
export const badAnswer = <Icon iconName={"Cancel"} styles={{root: {...defaultStyles, color: "red"}}}/>;
