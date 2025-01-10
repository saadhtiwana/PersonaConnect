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