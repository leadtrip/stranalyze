export type Activity = {
    id: number;
    athlete: Athlete;
    name: string;
    distance: number;
    moving_time: number;
    elapsed_time: number;
    total_elevation_gain?: number;
    sport_type: string;
    start_date: string;
    timezone: string;
    average_speed: number;
    max_speed: number;
    average_cadence?: number;
    average_watts?: number;
    weighted_average_watts?: number;
    kilojoules?: number;
    average_heartrate?: number;
    max_heartrate?: number;
    max_watts?: number;
    suffer_score: number;
}

export type Athlete = {
    id: number;
    firstname: string;
    lastname: string;
    city: string;
    state: string;
    country: string;
    sex: string;
}