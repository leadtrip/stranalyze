import { useEffect, useMemo, useState } from 'react';
import {
    MaterialReactTable,
    useMaterialReactTable,
    type MRT_ColumnDef,
    type MRT_ColumnFiltersState,
    type MRT_PaginationState,
    type MRT_SortingState,
} from 'material-react-table';
import {Activity} from "./types";
import {formatDateString, round, toHoursMinutesSeconds} from "./utils";


const Activities = () => {
    //data and fetching state
    const [data, setData] = useState<Activity[]>([]);
    const [isError, setIsError] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
    const [isRefetching, setIsRefetching] = useState(false);
    const [rowCount, setRowCount] = useState(0);

    //table state
    const [columnFilters, setColumnFilters] = useState<MRT_ColumnFiltersState>(
        [],
    );
    const [globalFilter, setGlobalFilter] = useState('');
    const [sorting, setSorting] = useState<MRT_SortingState>([]);
    const [pagination, setPagination] = useState<MRT_PaginationState>({
        pageIndex: 0,
        pageSize: 10,
    });

    //if you want to avoid useEffect, look at the React Query example instead
    useEffect(() => {
        const fetchData = async () => {
            if (!data.length) {
                setIsLoading(true);
            } else {
                setIsRefetching(true);
            }

            try {
                fetch('http://localhost:10051/stravaStore/activities'
                    ,{
                        headers : {
                            'Content-Type': 'application/json',
                            'Accept': 'application/json'
                        }
                    }
                )
                    .then(function(response){
                        console.log(response)
                        return response.json();
                    })
                    .then(function(myJson) {
                        console.log(myJson);
                        setData(myJson)
                        setRowCount(myJson.length)
                    });
            } catch (error) {
                setIsError(true);
                console.error(error);
                return;
            }
            setIsError(false);
            setIsLoading(false);
            setIsRefetching(false);
        };
        fetchData();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [
        columnFilters, //re-fetch when column filters change
        globalFilter, //re-fetch when global filter changes
        pagination.pageIndex, //re-fetch when page index changes
        pagination.pageSize, //re-fetch when page size changes
        sorting, //re-fetch when sorting changes
    ]);

    const columns = useMemo<MRT_ColumnDef<Activity>[]>(
        () => [
            {
              accessorKey: 'id',
              header: 'ID',
              enableHiding: true
            },
            {
                accessorKey: 'start_date',
                header: 'Started',
                size: 150,
                accessorFn: (originalRow) => formatDateString(originalRow.start_date)
            },
            {
                accessorKey: 'elapsed_time',
                header: 'Duration',
                size: 150,
                accessorFn: (originalRow) => toHoursMinutesSeconds(originalRow.elapsed_time)
            },
            {
                accessorKey: 'sport_type',
                header: 'Sport type',
                size: 150,
            },
            {
                accessorKey: 'name',
                header: 'Name',
                size: 150,
            },
            {
                accessorKey: 'average_watts',
                header: 'Avg Watts',
                size: 100,
                accessorFn: (originalRow) => round(originalRow.average_watts)
            },
            {
                accessorKey: 'max_watts',
                header: 'Max Watts',
                size: 100,
                accessorFn: (originalRow) => round(originalRow.max_watts)
            },
            {
                accessorKey: 'average_heartrate',
                header: 'Avg HR',
                size: 100,
                accessorFn: (originalRow) => round(originalRow.average_heartrate)
            },
            {
                accessorKey: 'max_heartrate',
                header: 'Max HR',
                size: 100,
                accessorFn: (originalRow) => round(originalRow.max_heartrate)
            },
            {
                accessorKey: 'suffer_score',
                header: 'Suffer score',
                size: 100,
            },
            {
                accessorKey: 'total_elevation_gain',
                header: 'Elevation gain',
                size: 100,
            },

        ],
        [],
    );

    const table = useMaterialReactTable({
        columns,
        data,
        initialState: {
            showColumnFilters: true,
            /*columnVisibility: { id: false },*/
            sorting: [
                {
                    id: 'id',
                    desc: true,
                }
            ]
        },
        onPaginationChange: setPagination,
        onSortingChange: setSorting,
        rowCount,
        state: {
            columnFilters,
            globalFilter,
            isLoading,
            pagination,
            showAlertBanner: isError,
            showProgressBars: isRefetching,
            sorting,
        },
    });

    return <MaterialReactTable table={table} />;
};

export default Activities;