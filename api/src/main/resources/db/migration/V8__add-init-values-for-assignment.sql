INSERT INTO `assignment` (
                `id`,
                `title`,
                `task`,
	            `course`
	        )
	 VALUES (
	            '23a2027e-2e16-4891-a8e7-4aeee8781994',
	            'test assignment',
	            'add assignments and implement AI plagiarism detection',
	            '4bc9f0ab-c87e-4473-a3ec-192ab8652d41'
            );
INSERT INTO `assignment` (
				`id`,
                `parent_assignment`,
                `student`
			)
	 VALUES (
	 	        '6605b8cd-0620-4c29-b4cf-ecfa912f17c0',
	 	        '23a2027e-2e16-4891-a8e7-4aeee8781994',
	 	        '20b8f744-2d28-417c-a917-1acf0ffbb386'
	        );