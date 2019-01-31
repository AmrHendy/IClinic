-- Restore All files into the database.
-- if you want to change the files path change them.
-- As Example if you want to save files on Desktop then make the path 'path to desktop/filename.txt' 

use iclinic;

LOAD DATA INFILE 'D:/user.txt' IGNORE
INTO TABLE user
CHARACTER SET utf8
FIELDS TERMINATED BY ','
LINES TERMINATED BY '\n';

LOAD DATA INFILE 'D:/patient.txt' IGNORE
INTO TABLE patient
CHARACTER SET utf8
FIELDS TERMINATED BY ','
LINES TERMINATED BY '\n';

LOAD DATA INFILE 'D:/appointment.txt' IGNORE
INTO TABLE appointment
CHARACTER SET utf8
FIELDS TERMINATED BY ','
LINES TERMINATED BY '\n';

