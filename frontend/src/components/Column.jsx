export const columns = [
	{
		accessorKey: 'id',
		header: '#',
	},
	{
		accessorKey: 'github_id',
		header: 'user name',
		cell: ({ row }) => row.original.github_id ?? 'Anonymous',
	},
	{
		accessorKey: 'before_carbon',
		header: 'before carbon emission',
		cell: ({ row }) => `${row.original.before_carbon.toFixed(2)} g`,
	},
	{
		accessorKey: 'after_carbon',
		header: 'after carbon emission',
		cell: ({ row }) => `${row.original.after_carbon.toFixed(2)} g`,
	},
	{
		accessorKey: 'energy_needed',
		header: 'Needed Energy',
		cell: ({ row }) => `${row.original.energy_needed.toFixed(2)} g`,
	},
	{
		accessorKey: 'date',
		header: 'Date',
	},
];
