/**
 * Created by Abdelkader Midani on 27/10/14.
 */
var myApp = angular.module('myApp',['angularFileUpload']);

myApp.controller('MyController', ['$scope','$http', 'FileUploader', function($scope, $http, FileUploader) {

    $scope.uploaded = false;
    $scope.displaySpinner = false;

    $scope.bestJourney = {};

    var uploader = $scope.uploader = new FileUploader({
        url: '/restApi/init'
    });


    // FILTERS

    uploader.filters.push({
        name: 'customFilter',
        fn: function(item /*{File|FileLikeObject}*/, options) {
            return this.queue.length < 10;
        }
    });

    //
    $scope.removeFile = function(item){
        item.remove();
        $scope.uploaded = false;
    }

    $scope.removeAll = function(uploader){
        uploader.clearQueue();
        $scope.uploaded = false;
    }

    // CALLBACKS

    uploader.onWhenAddingFileFailed = function(item /*{File|FileLikeObject}*/, filter, options) {
        console.info('onWhenAddingFileFailed', item, filter, options);
    };
    uploader.onAfterAddingFile = function(fileItem) {
        console.info('onAfterAddingFile', fileItem);
        $scope.uploaded = false;
        angular.element.find('#singleFileUpload')[0].value = '';
    };
    uploader.onAfterAddingAll = function(addedFileItems) {
        console.info('onAfterAddingAll', addedFileItems);
    };
    uploader.onBeforeUploadItem = function(item) {
        console.info('onBeforeUploadItem', item);
        $scope.displaySpinner = true;
    };
    uploader.onProgressItem = function(fileItem, progress) {
        console.info('onProgressItem', fileItem, progress);
    };
    uploader.onProgressAll = function(progress) {
        console.info('onProgressAll', progress);
    };
    uploader.onSuccessItem = function(fileItem, response, status, headers) {
        console.info('onSuccessItem', fileItem, response, status, headers);
    };
    uploader.onErrorItem = function(fileItem, response, status, headers) {
        console.info('onErrorItem', fileItem, response, status, headers);
    };
    uploader.onCancelItem = function(fileItem, response, status, headers) {
        console.info('onCancelItem', fileItem, response, status, headers);
    };
    uploader.onCompleteItem = function(fileItem, response, status, headers) {
        console.info('onCompleteItem', fileItem, response, status, headers);
        $http.get('/restApi/journey').
            success(function (data, status, headers, config) {
                $scope.bestJourney = data;
                $scope.uploaded = true;
                $scope.displaySpinner = false;
            }).
            error(function (data, status, headers, config) {
                $scope.bestJourney = {};
                $scope.displaySpinner = false;
            });
    };
    uploader.onCompleteAll = function() {
        console.info('onCompleteAll');
    };

    console.info('uploader', uploader);


}]);
