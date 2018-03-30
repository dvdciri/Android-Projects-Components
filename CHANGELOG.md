# Change Log
All notable changes to this project will be documented in this file.

The format is based on [Keep a Changelog](http://keepachangelog.com/)
and this project adheres to [Semantic Versioning](http://semver.org/).

## [Unreleased]
### Changed
### Deprecated
### Added
- Added `notifyAdapterDataSetChanged()` method to the MultiChoiceAdapter in order to be able to keep a valid internal state of the selected/deselected items. This will then delegate the actual `RecyclerView.Adapter#notifyDataSetChanged()` to do the rest.

### Fixed
### Deleted

## [2.3.0]
### Changed
- `MultiChoiceToolbar.Builder#setTitles()` is now available as `(String, String)` or `(@StringRes int, @PluralsRes int)`

### Deprecated
- Marked `MultiChoiceToolbar.Builder#setTitles(String, @PluralsRes int)` as deprecated in favor of `(@StringRes int, @PluralsRes int)`

### Added
- Added possibility to let the library handle the savedInstanceState in order to keep the selection in place when orientation
and other configuration change
- Added `deselect(int position)` method to the MultiChoiceAdapter


## [2.1.0]
### Added
- Possibility to use QuantityStrings for the toolbar while selecting items

## [2.0.0]
### Changed
- Moved logic related to the MultiChoiceRecyclerView class into the MultiChoiceAdapter
- Internal improvements getting rid of holding views states
- Aggregated some of the method of the MultiChoiceToolbar Builder like titles, defaultColors, selectedColors and change "setIcon" to "setDefaultIcon"

### Deleted
- MultiChoiceRecyclerView class

## [1.2.14]
### Added
- Possibility to disable some items from the selection mode with the method isSelectableInMultiChoiceMode(int position)

### Changed
- Clean and polish

### Fixed
- Kept order of selected items when retrieved back with "getSelectedItems"
- Performance improvements

### Deleted
- Application tag from library manifest
- Removed unnecessary methods like setColumnNumber ad setRowNumber

## [1.1.8]
### Added
- Changelog file
- Class MultiChoiceToolbar for wrapping all the multi choice toolbar related fields
- Toolbar icon support for MultiChoiceToolbar (Builder)

### Changed
- Method setMultiChoiceToolbar() now accept a MultiChoiceToolbar class with its builder for a simpler construction
- Internal file structure in order to allow package-protected classes/methods
- Updated sample with new toolbar implementation

## [1.1.7]
### Fixed
- Circle CI build failing because of upgrade needed to JDK8

## [1.1.6]
### Fixed
- Wrong callback on deselectAll
