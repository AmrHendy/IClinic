-- Backup All data into files.
-- if you want to change the files path change them.
-- As Example if you want to save files on Desktop then make the path 'path to desktop/filename.txt' 

use iclinic;

SELECT * INTO OUTFILE 'D:/user.txt'
CHARACTER SET utf8
FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n'
From user;

SELECT * INTO OUTFILE 'D:/patient.txt'
CHARACTER SET utf8
FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n'
From patient;

SELECT * INTO OUTFILE 'D:/appointment.txt'
CHARACTER SET utf8
FIELDS TERMINATED BY ',' LINES TERMINATED BY '\n'
From appointment;
