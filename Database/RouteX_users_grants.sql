-- ===============================
-- RouteX - Users & Permissions
-- ===============================

DROP USER IF EXISTS `login_User`@`%`;
DROP USER IF EXISTS `traveler`@`%`;
DROP USER IF EXISTS `admin_amministratore_routex`@`%`;

-- ===============================
-- CREATE USERS WITH PASSWORDS
-- ===============================

CREATE USER `login_User`@`%`
IDENTIFIED BY 'login_pass';

CREATE USER `traveler`@`%`
IDENTIFIED BY 'traveler';

CREATE USER `admin_amministratore_routex`@`%`
IDENTIFIED BY 'adminroute';

-- ===============================
-- LOGIN USER
-- ===============================
GRANT USAGE ON *.* TO `login_User`@`%`;
GRANT EXECUTE ON PROCEDURE `RouteX_Update`.`getallcity` TO `login_User`@`%`;
GRANT EXECUTE ON PROCEDURE `RouteX_Update`.`getfermatabyid` TO `login_User`@`%`;
GRANT EXECUTE ON PROCEDURE `RouteX_Update`.`login_user` TO `login_User`@`%`;
GRANT EXECUTE ON PROCEDURE `RouteX_Update`.`restituiscistazioni` TO `login_User`@`%`;
GRANT EXECUTE ON PROCEDURE `RouteX_Update`.`register` TO `login_User`@`%`;
GRANT EXECUTE ON PROCEDURE `RouteX_Update`.`register_User` TO `login_User`@`%`;

-- ===============================
-- TRAVELER
-- ===============================
GRANT USAGE ON *.* TO `traveler`@`%`;
GRANT EXECUTE ON PROCEDURE `RouteX_Update`.`getAllCity` TO `traveler`@`%`;
GRANT EXECUTE ON PROCEDURE `RouteX_Update`.`RestituisciStazioni` TO `traveler`@`%`;
GRANT EXECUTE ON PROCEDURE `RouteX_Update`.`GetFermataById` TO `traveler`@`%`;
GRANT EXECUTE ON PROCEDURE `RouteX_Update`.`getpath` TO `traveler`@`%`;
GRANT EXECUTE ON PROCEDURE `RouteX_Update`.`savepayment` TO `traveler`@`%`;
GRANT EXECUTE ON PROCEDURE `RouteX_Update`.`saveroute` TO `traveler`@`%`;
GRANT EXECUTE ON PROCEDURE `RouteX_Update`.`DeleteRouteBySignature` TO `traveler`@`%`;
GRANT EXECUTE ON PROCEDURE `RouteX_Update`.`DeleteAllRoutes` TO `traveler`@`%`;
GRANT EXECUTE ON PROCEDURE `RouteX_Update`.`DeleteTicketByCode` TO `traveler`@`%`;
GRANT EXECUTE ON PROCEDURE `RouteX_Update`.`DeleteAllTickets` TO `traveler`@`%`;
GRANT EXECUTE ON PROCEDURE `RouteX_Update`.`getMessages` TO `traveler`@`%`;
GRANT EXECUTE ON PROCEDURE `RouteX_Update`.`spCommunication` TO `traveler`@`%`;
GRANT EXECUTE ON PROCEDURE `RouteX_Update`.`MarkCommunicationAsRead` TO `traveler`@`%`;
GRANT EXECUTE ON `RouteX_Update`.* TO `traveler`@`%`;

-- ===============================
-- ADMIN
-- ===============================
GRANT USAGE ON *.* TO `admin_amministratore_routex`@`%`;
GRANT EXECUTE ON PROCEDURE `RouteX_Update`.`getallpathinfo` TO `admin_amministratore_routex`@`%`;
GRANT EXECUTE ON PROCEDURE `RouteX_Update`.`getmessages` TO `admin_amministratore_routex`@`%`;
GRANT EXECUTE ON PROCEDURE `RouteX_Update`.`spcommunication` TO `admin_amministratore_routex`@`%`;
GRANT EXECUTE ON PROCEDURE `RouteX_Update`.`spsolvedmessage` TO `admin_amministratore_routex`@`%`;
GRANT EXECUTE ON PROCEDURE `RouteX_Update`.`ListAdmins` TO `admin_amministratore_routex`@`%`;
GRANT EXECUTE ON PROCEDURE `RouteX_Update`.`CreateAdmin` TO `admin_amministratore_routex`@`%`;
GRANT EXECUTE ON PROCEDURE `RouteX_Update`.`DeleteAdminByCodiceFiscale` TO `admin_amministratore_routex`@`%`;
GRANT EXECUTE ON PROCEDURE `RouteX_Update`.`ListTravelers` TO `admin_amministratore_routex`@`%`;
GRANT EXECUTE ON PROCEDURE `RouteX_Update`.`DeleteTravelerByCodiceFiscale` TO `admin_amministratore_routex`@`%`;
GRANT EXECUTE ON PROCEDURE `RouteX_Update`.`ApproveCommunication` TO `admin_amministratore_routex`@`%`;
GRANT EXECUTE ON PROCEDURE `RouteX_Update`.`ApproveTravelerCommunication` TO `admin_amministratore_routex`@`%`;
GRANT EXECUTE ON PROCEDURE `RouteX_Update`.`RejectTravelerCommunication` TO `admin_amministratore_routex`@`%`;
GRANT EXECUTE ON PROCEDURE `RouteX_Update`.`DeleteCommunication` TO `admin_amministratore_routex`@`%`;

FLUSH PRIVILEGES;
