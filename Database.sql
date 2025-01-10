CREATE TABLE User_ (
    userID VARCHAR(50) PRIMARY KEY,
    firstName VARCHAR(100) NOT NULL,
    lastName VARCHAR(100) NOT NULL,
    middleName VARCHAR(100) NULL,
    preferredName VARCHAR(100) NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(15) NULL,
    organizationUnitID VARCHAR(50)
);

CREATE TABLE Checklist (
    checklistID VARCHAR(50) PRIMARY KEY,
    type VARCHAR(50) NOT NULL,
    description VARCHAR(255) NOT NULL,
    orgUnitID VARCHAR(50)
);

CREATE TABLE OrganizationalUnit (
    orgUnitID VARCHAR(50) PRIMARY KEY,
    unitName VARCHAR(255) NOT NULL,
    unitType VARCHAR(50) NOT NULL,
    code VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    effectiveDate DATE NOT NULL,
    description VARCHAR(255) NOT NULL,
    createdAt DATE NOT NULL
);

CREATE TABLE SearchMatch (
    searchID VARCHAR(50) PRIMARY KEY,
    criteria VARCHAR(255) NOT NULL
);

CREATE TABLE CommunityDirectory (
    directoryID VARCHAR(50) PRIMARY KEY,
    userID VARCHAR(50) NOT NULL,
    userType VARCHAR(50) NOT NULL
);

CREATE TABLE Phone (
    phoneID VARCHAR(50) PRIMARY KEY,
    number_ VARCHAR(15) NOT NULL,
    type VARCHAR(50) NOT NULL,
    userID VARCHAR(50)
);

CREATE TABLE EmergencyContact (
    contactID VARCHAR(50) PRIMARY KEY,
    userID VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL,
    relationship VARCHAR(50) NOT NULL,
    phone VARCHAR(15) NOT NULL,
    email VARCHAR(255) NOT NULL
);

CREATE TABLE BiographicDetails (
    detailID VARCHAR(50) PRIMARY KEY,
    userID VARCHAR(50) NOT NULL,
    workExperience VARCHAR(255) NOT NULL,
    healthInfo VARCHAR(255) NOT NULL,
    languages VARCHAR(255) NOT NULL,
    visaDetails VARCHAR(255) NOT NULL,
    passportDetails VARCHAR(255) NOT NULL,
    educationHistory VARCHAR(255) NOT NULL
);


ALTER TABLE User_
ADD CONSTRAINT fk_organizationUnitID
FOREIGN KEY (organizationUnitID) 
REFERENCES OrganizationalUnit(orgUnitID);

ALTER TABLE Checklist
ADD CONSTRAINT fk_checklist_orgunit
FOREIGN KEY (orgUnitID) 
REFERENCES OrganizationalUnit(orgUnitID);

ALTER TABLE CommunityDirectory
ADD CONSTRAINT fk_communitydirectory_user
FOREIGN KEY (userID) 
REFERENCES User_(userID);

ALTER TABLE Phone
ADD CONSTRAINT fk_phone_user
FOREIGN KEY (userID) 
REFERENCES User_(userID);

ALTER TABLE EmergencyContact
ADD CONSTRAINT fk_emergencycontact_user
FOREIGN KEY (userID) 
REFERENCES User_(userID);

ALTER TABLE BiographicDetails
ADD CONSTRAINT fk_biographicdetails_user
FOREIGN KEY (userID) 
REFERENCES User_(userID);


