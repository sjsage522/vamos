var main = {
    init : function () {
        var _this = this;
        $('#btn-save').on('click', function () {
            _this.save();
        });

        $('#btn-update').on('click', function () {
            _this.update();
        });

        $('#btn-delete').on('click', function () {
            _this.delete();
        });
    },
    save : function () {
        let data = new FormData();
        data.append("title", $('#title').val())
        data.append("content", $('#content').val())
        data.append("price", $('#price').val())
        data.append("categoryNameEN", $('#category').val())

        $.ajax({
            type: 'POST',
            url: '/api/board',
            data: data,
            dataType: 'json',
            processData: false,
            contentType: false,
            cache: false,
        }).done(function() {
            alert('글이 등록되었습니다.');
            window.location.href = '/';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    update : function () {
        let data = new FormData();
        data.append("title", $('#title').val())
        data.append("content", $('#content').val())
        data.append("price", $('#price').val())
        data.append("categoryNameEN", $('#category').val())

        var id = $('#id').val();

        $.ajax({
            type: 'PATCH',
            url: '/api/board/'+id,
            data: data,
            dataType: 'json',
            processData: false,
            contentType: false,
            cache: false,
        }).done(function() {
            alert('글이 수정되었습니다.');
            window.location.href = '/';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    },
    delete : function () {
        var id = $('#id').val();

        $.ajax({
            type: 'DELETE',
            url: '/api/board/'+id,
            dataType: 'json',
            contentType:'application/json; charset=utf-8'
        }).done(function() {
            alert('글이 삭제되었습니다.');
            window.location.href = '/';
        }).fail(function (error) {
            alert(JSON.stringify(error));
        });
    }

};

main.init();