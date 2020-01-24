import React from "react";
import {Spinner, SpinnerSize, Stack} from "office-ui-fabric-react";

export const PageSpinner: React.FC = () => (
    <Stack style={{height: "100%"}} verticalAlign="center">
        <Spinner size={SpinnerSize.large}/>
    </Stack>
);
