import stravaConnect from './/images/strava-api-logos/connect-with/btn_strava_connectwith_orange.png'
import stravaCompatibleWith from './/images/strava-api-logos/compatible-with-strava/cptblWith_strava_light/api_logo_cptblWith_strava_horiz_light.png'
import React from "react";

function Header() {
    return (
        <>
            <div style={{display: "flex", flexDirection: "row", justifyContent: "space-between"}}>
                <h1 className="header">stranalyze</h1>
                <img src={stravaConnect} alt="Strava connect" />
                <img src={stravaCompatibleWith} alt="Strava compatible with" />
            </div>
        </>
    )
}

export default Header;