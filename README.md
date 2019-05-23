# TextMaster extension for SAP Commerce Cloud (Hybris)

[![Build Status](https://travis-ci.org/joemccann/dillinger.svg?branch=master)](https://travis-ci.org/joemccann/dillinger)

TextMaster is the world’s first global translation solution that is available entirely online. Thanks to a network of expert translators, cutting-edge technologies and a range of value-added services, TextMaster enables companies to streamline the translation of their content in over 50 languages and areas of expertise.

# Description
***

The Textmaster extension with SAP Hybris allows you to easily translate your content within Hybris to a large quantity of languages with a simple mass edit process:

- Products
- Categories
- WCMS items

# Features
***

  - All content types (product, categories, WCMS items) and attributes handled
  - No licence or set up costs: we just charge content translated
  - Advanced filtering and selection of items
  - Quote with live translation memory analysis
  - Seamless and automatic translation workflow
  - Real-time status tracking
  - Integrated reviewing process

# Requirements
***

We suggest the following steps in order for the integration to go as smoothly as possible:

### Create a TextMaster account 

Creating your account on [TextMaster](https://textmaster.com) is totally free. You can create one [here](https://app.textmaster.com/sign_in).

### Add credits to your account

You won't be able to launch a translation request without credits. Please make sure that you have enough credits on your account, otherwise the request you're sending will remain in creation.

### Create an API template in the language pair of your choice

The API template is like a project model where you can pre-select all the translation options of your choice (translation memory, glossaries, favorite authors, expertise, etc.). This template is necessary for creating a translation request. You need to create one per language pair. Once you're done with it, you won't have to select your options every time you want to send an item for translation.


_Note: For the purpose of your tests, we also have a test environment at your disposal where you can test the whole translation workflow. Please [contact us](integrations@textmaster.com) if you are interested._

# Compatibility
***

- You will need Hybris 5.7 or higher
- A storefront is needed to access to product preview in backoffice.

# How it works
***

### Components
The TextMaster menu will appear under the "Administration" tab of your Hybris backoffice. There are three tabs: 

- _Create project_: this tab allows you to initiate a content search, select your content and send it for translation
- _Dashboard_: the dashboard allows you to see all the projects that were sent for translation, follow their status, review the translations and validate them
- _TextMaster Configuration_: that's where you will enter your API key and API secret to connect SFCC to your TextMaster account as well as register default languages, content types and attributes

### Setup
**1  - Authentication with TextMaster**

- Go to *Administration > TextMaster Configuration* 
- Select 'New' on the right hand side of the screen to create a new account connection
- Enter your account name, your API and API secret: you will find them on your TextMaster account [here](https://app.textmaster.com/clients/api_info)
- Press 'DONE'

**2 - Attribute setup**

- Go to *Administration > TextMaster Configuration*
- Under 'TextMaster Account', select the account you just created

You'll be able to choose default source and target languages as well as the default attributes per content type. It means those attributes and languages will be pre-selected every time you want to create a new translation request. You will, of course, be able to add or remove attributes from the selection during the project creation process.

### Creating a translation project

- Go to *Administration > TextMaster Configuration* 
- Start with choosing your languages then select your templates
- Select your content type and the attributes you wish to send for translation then 'Validate and select items'
- Select your content. If you're looking for specific content, you can initiate a content search according to different values such as catalog name, product type, last update, creation date, etc.
- Place order

The project will be immediately created on TextMaster! You can also see it within Hybris on your translation dashboard with the status 'In creation' (go to Administration > Dashboard). The final price will be available on your dashboard after a short while. If you activated the Translation Memory option, it can take a bit longer. You can press 'Refresh' next to your project to update the price. Once it's available, you'll simply have to click on 'Start' to launch the project. It will switch from 'In creation' to 'In progress'.

### Reviewing a translation
When an item is translated by an author, it goes from 'In progress' to 'In review'. To review the translation,

- Go to *Administration > Dashboard*
- Select the project in which you included the item
- Select the item you're looking for. A window will open on the left
- In the section Translations > Attribute, click on 'Edit details'. You'll be able to see the original content and the translation for each attribute

### Validate a translation
If you're satisfied with a translation, select one or more items, and click on 'Validate documents'

# Installation
***
- Put the module into your personal module space (for instance, hybris/bin/custom/)
- Activate the module in the hybris/config/localextensions.xml file, adding the following line:

```sh
<!-- TextMaster Extensions -->
<extension name="textmastercore" />
<extension name="textmasterbackoffice" />
```
- Compile extensions:

```sh
ant all
```
- Update you system to deploy new item types:

```sh
ant updatesystem
```

# Properties keys
----
The TextMaster module is based on some properties you could override in your project context.

### Code generators
**TexMasterAccount Item**

|Key|Description|Default value|
|---|---|---|
|keygen.textmasteraccount.code.name|Name of the key generator|textmasteraccount|
|keygen.textmasteraccount.code.digits|Number of digits composing the value|8|
|keygen.textmasteraccount.code.start|Start indicator|00000000|
|keygen.textmasteraccount.code.template|Template|$|

**TextMasterProject Item**

|Key|Description|Default value|
|---|---|---|
|keygen.textmasterproject.code.name|Name of the key generator|textmasterproject|
|keygen.textmasterproject.code.digits|Number of digits composing the value|8|
|keygen.textmasterproject.code.start|Start indicator|00000000|
|keygen.textmasterproject.code.template|Template|$|

**TextMasterDocument Item**

|Key|Description|Default value|
|---|---|---|
|keygen.textmasterdocument.code.name|Name of the key generator|textmasterdocument|
|keygen.textmasterdocument.code.digits|Number of digits composing the value|8|
|keygen.textmasterdocument.code.start|Start indicator|00000000|
|keygen.textmasterdocument.code.template|Template|$|

**TextMasterDocumentAttribute Item**

|Key|Description|Default value|
|---|---|---|
|keygen.textmasterdocumentattribute.code.name|Name of the key generator|textmasterdocumentattribute|
|keygen.textmasterdocumentattribute.code.digits|Number of digits composing the value|8|
|keygen.textmasterdocumentattribute.code.start|Start indicator|00000000|
|keygen.textmasterdocumentattribute.code.template|Template|$|

**TextMasterSupportMessage Item**

|Key|Description|Default value|
|---|---|---|
|keygen.textmastersupportmessage.code.name|Name of the key generator|textmastersupportmessage|
|keygen.textmastersupportmessage.code.digits|Number of digits composing the value|8|
|keygen.textmastersupportmessage.code.start|Start indicator|00000000|
|keygen.textmastersupportmessage.code.template|Template|$|

**TextMasterConfiguration Item**

|Key|Description|Default value|
|---|---|---|
|keygen.textmasterconfiguration.code.name|Name of the key generator|textmasterconfiguration|
|keygen.textmasterconfiguration.code.digits|Number of digits composing the value|8|
|keygen.textmasterconfiguration.code.start|Start indicator|00000000|
|keygen.textmasterconfiguration.code.template|Template|$|

**TextMasterItemType Item**

|Key|Description|Default value|
|---|---|---|
|keygen.textmasteritemtype.code.name|Name of the key generator|textmasteritemtype|
|keygen.textmasteritemtype.code.digits|Number of digits composing the value|8|
|keygen.textmasteritemtype.code.start|Start indicator|00000000|
|keygen.textmasteritemtype.code.template|Template|$|

### API Configuration

|Key|Description|Default value|
|---|---|---|
|textmastercore.baseurl|URL of the TextMaster platform|https://api.textmastersandbox.com/|
The url above is that of Textmaster's sandbox environment. It will allow you to test the translation workflow.
Once your tests are over, you will be able to switch to the production environment - https://api.textmaster.com/

### Strategies configuration

|Key|Description|Default value|
|---|---|---|
|textmastercore.catalog.staged.update|Define whether the translation needs to be applied to the STAGED catalog automatically, or if this process needs to be managed manually (in case of automatic status changes rules for published or unpublished products)|true|
|textmastercore.documents.selection.max|Define the maximum number of documents to add to the project (if it needs to be restricted). If no maximum size needs to be applied, define "-1" value|1000|
|textmastercore.documents.batch.size|Define the maximum number of documents to send in one batch|100|
|textmastercore.project.send.retry.interval|Define the time to wait between 2 attempts of project creation, in minutes|2|
|textmastercore.project.finalize.schedule|Define the time to wait before finalizing the project, in minutes|2|
|textmastercore.project.autolaunch.retry.max|Define the number of times the project status must be retrieved for autolaunch projects|3|
|textmastercore.project.autolaunch.schedule|Define the time to wait before finalizing the project, in minutes|2|

# First configuration
---
### Cronjobs
By default, these jobs are imported but not automatically triggered:

|Job|Description|
|---|---|
|textMasterGetTranslationCronjob|Get all translated documents|
|textMasterDocumentRecoveryCronjob|Synchronize documents between Hybris and the TextMaster platform|
|textMasterGetSupportMessagesCronjob|Retrieve support messages from the TextMaster platform|

### Language
A mapping must be configured between the existing languages in Hybris and the languages configured on the TextMaster platform, through IMPEX script.

*For example:*

```sh
# Import languages mapping
INSERT_UPDATE TextMasterLanguage; isocode[unique=true]; active[default=true]; language(isocode)
                                ; fr-FR               ;                     ; fr
                                ; en-GB               ;                     ; en
                                ; es-ES               ;                     ; es
                                ; it-IT               ;                     ; it
                                ; de-DE               ;                     ; de
```

# Backoffice Configuration
---

### Items pages for list
You have the possibility, if you wish, to configure the number of elements displayed per page in the CollectionBrowser component.

**Manual way**

To define this value, you must follow the following steps:
1. Use the F4 button to switch to configuration mode
2. Select the pen shaped icon in order to edit the configuration of the component you
wish to change
3. You can then enter the value you want in the pageSize attribute

**Packaged way**

To package the value, you can create an extension backoffice and enter <your-extension-backoffice>-backoffice-widgets.xml to override default widget configuration, by setting the property:

```sh
...
<setting key="pageSize" type="Integer">50</setting>
...
```







