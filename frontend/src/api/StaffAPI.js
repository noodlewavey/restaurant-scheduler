export const getAllStaffMembers = async () => {
    try {
        const response = await fetch('/staff/get-staff-members-list');
        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || 'Failed to fetch staff members');
        }
        return await response.json();
    } catch (error) {
        console.error('Error fetching staff members:', error);
        throw error;
    }
};

export const saveStaffMember = async (staffData) => {
    try {
        const response = await fetch('/staff/save-staff-member', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(staffData)
        });
        if (!response.ok) {
            const errorData = await response.json();
            throw new Error(errorData.message || 'Failed to save staff member');
        }
        return await response.json();
    } catch (error) {
        console.error('Error saving staff member:', error);
        throw error;
    }
}; 