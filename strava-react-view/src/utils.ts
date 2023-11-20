export function formatDateString(dateStr: string) {
    return new Date(Number(dateStr)).toString();
}

export function toHoursMinutesSeconds(field: number) {
    const hours = field / 3600;
    const minutes = (field % 3600) / 60;
    const seconds = field % 60;
    return `${formatDatePart(hours)}:${formatDatePart(minutes)}:${formatDatePart(seconds)}`;
}

function formatDatePart(num: number) {
    return Math.round(num).toString().padStart(2,'0');
}

export function round(field?: number) {
    return field ? Math.round(field) : ''
}