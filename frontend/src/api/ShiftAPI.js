export const createShift = async (shiftData) => {
    try {
        const response = await fetch('/shift/create-shift', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(shiftData)
        });
        if (!response.ok) {
            throw new Error('Failed to create shift');
        }
        return await response.json();
    } catch (error) {
        console.error('Error creating shift:', error);
        throw error;
    }
};

export const assignShift = async (shiftId, staffId) => {
    try {
        const response = await fetch(`/shift/${shiftId}/assign?staffId=${staffId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            }
        });
        if (!response.ok) {
            throw new Error('Failed to assign shift');
        }
        return await response.json();
    } catch (error) {
        console.error('Error assigning shift:', error);
        throw error;
    }
}; 