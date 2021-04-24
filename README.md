# CLICKBAIT-FILTER-JAVA-SERVICE

[![ClickBaitSite](https://click-bait-filtering-plugin.com/assets/images/icon-128-122x122.png)](https://click-bait-filtering-plugin.com/index.html)

## Description

This service is a part of a group of services who plot to rid the web of clickbait by relying on user input and machine learning. The completed application functions by storing its user clicked items and using them to disseminate what is clickbait and what is legitimate news, stories, etc. This is done in conjunction with a machine learning classificator. The full application functions on all sites and thus can allow you to be more productive while browsing the web. It functions by providing the user with a slider that givies the possibility to filter content deemed clickbait or highlight content that is deemed not. The application also shows its user a topology of the most clickbaity content of each domain.
</br>
</br>
This service is essentially a backend that authenticates/identifies the user and stores/rates his clicks by requests to [click_bait_filter_j]. This service also maintains db sanity (scheduler) and maps user visited domains. For more info visit the application [CLICKBAIT-PORTAL] and download the user application/plugin from the [CHROME-STORE].

## Technologies

CLICKBAIT-FILTER-SERVICE uses a number of open source projects:

  * [JAVA11] - JAVA 11 SDK
  * [SPRING] - JAVA SPRING FRAMEWORK
  * [SPRING-BOOT] - SPRING PROJ BOOTSTRAP
  * [POSTGRESQL] - SQL DATABASE
  * [JWT] - JSON WEB TOKEN

## Installation

CLICKBAIT-FILTER-SERVICE requires [Node.js](https://nodejs.org/) v10+ to run.

Install node dependancies for the project:
```sh
$ cd click_bait_filter_be
$ npm install
```

## Applications Scopes

This service is a part of a multi application project that features the following git repositories:

| Service Name                                  | Description                         | Maintainer              |
| ----------------------------------------      |:------------------------------------|:------------------------|
| [click_bait_filter_extension]                 | Chrome Extensions Plugin            | [LeadShuriken]          |
| [click_bait_filter_be]\(PROTO)          | Node Application Test Server        | [LeadShuriken]          |
| [click_bait_filter_j]                         | Spring Production Server            | [LeadShuriken]          |
| [click_bait_filter_tflow]                     | Java Tensor Flow Server             | [LeadShuriken]          |
| [click_bait_filter_ml]                        | TensorFlow Model Generator/Updater  | [LeadShuriken]          |
| [click_bait_filter_portal]                    | Service and Information Portal      | [LeadShuriken]          |


For development the application should have the following structure:
```sh
 | .
 | +-- click_bait_filter_extension
 | +-- click_bait_filter_be
 | +-- click_bait_filter_j
 | +-- click_bait_filter_tflow
 | +-- click_bait_filter_ml
 | +-- click_bait_filter_portal
```
This is as the 'click_bait_filter_ml' uses the 'click_bait_filter_be' api's for filtering links. 'click_bait_filter_portal' is just static html which can preside anywhere. 

## Running and Building

This application is an **Express Application**;

### 1. Runing the service
---

* **WITH MICROSOFT VISUAL STUDIO CODE**

  To run the application, open the project in Microsoft VS Code and navigate to the .vscode folder.
  
  There you will see the **launch.json** file. And create this run configuration:
  
  ```sh
  {
      "type": "node",
      "request": "launch",
      "name": "LAUNCH",
      "env": {
          "API_URL": "/api",
          "MONGODB_URI": "<MONGO CONNECTION STRING>",
          "PORT": "4000"
      },
      "program": "${workspaceFolder}/index.js",
      "restart": true,
      "console": "integratedTerminal",
      "internalConsoleOptions": "neverOpen"
  }
  ```
  More information on [MONGO CONNECTION STRING] formats.

* **WITH CLI COMMANDS**

  Open the terminal and navigate to the root project folder.

  ```sh
  $ export API_URL=/api && export MONGODB_URI=<MONGO CONNECTION STRING> && export PORT=4000 && node index.js
  ```

  This launches the service on: **http://localhost:4000** 

### Todos

 - Tests and Docs
 
  [JAVA11]: <https://www.oracle.com/java/technologies/javase-jdk11-downloads.html>
  [SPRING]: <https://spring.io>
  [SPRING-BOOT]: <https://spring.io/projects/spring-boot>
  [POSTGRESQL]: <https://www.postgresql.org>
  [JWT]: <https://jwt.io>

  [webpack-chrome-extension-reloader]: <https://github.com/LeadShuriken/webpack-chrome-extension-reloader>
  [click_bait_filter_extension]: <https://github.com/LeadShuriken/click_bait_filter_extension>
  [click_bait_filter_be]: <https://github.com/LeadShuriken/click_bait_filter_be>
  [click_bait_filter_ml]: <https://github.com/LeadShuriken/click_bait_filter_ml>
  [click_bait_filter_portal]: <https://github.com/LeadShuriken/click_bait_filter_portal>
  [click_bait_filter_j]: <https://github.com/LeadShuriken/click_bait_filter_j>
  [click_bait_filter_tflow]: <https://github.com/LeadShuriken/click_bait_filter_tflow>

  [LeadShuriken]: <https://github.com/LeadShuriken>
  [rubenspgcavalcante]: <https://github.com/rubenspgcavalcante>

  [CHROME-STORE]: <https://chrome.google.com/webstore/detail/clickbait-filtering-plugi/mgebfihfmenffogbbjlcljgaedfciogm>
  [CLICKBAIT-PORTAL]: <https://click-bait-filtering-plugin.com>
