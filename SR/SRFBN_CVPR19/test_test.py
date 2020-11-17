import argparse, time, os
import imageio

import options.options as option
from utils import util
from solvers import create_solver
from data import create_dataloader
from data import create_dataset
import data.common as common

import torch.utils.data

def dataloader(dataset):
    # phase = dataset_opt['phase']
    batch_size = 1
    shuffle = False
    num_workers = 1
    return torch.utils.data.DataLoader(
        dataset, batch_size=batch_size, shuffle=shuffle, num_workers=num_workers, pin_memory=True)

def dataset(dataset_opt):
    mode = dataset_opt['mode'].upper()
    if mode == 'LR':
        from data.LR_dataset import LRDataset as D
    elif mode == 'LRHR':
        from data.LRHR_dataset import LRHRDataset as D
    else:
        raise NotImplementedError("Dataset [%s] is not recognized." % mode)
    dataset = D(dataset_opt)
    print('===> [%s] Dataset is created.' % (mode))
    return dataset

def loading_model():
    parser = argparse.ArgumentParser(description='Test Super Resolution Models')
    parser.add_argument('-opt', type=str, required=True, help='Path to options JSON file.')
    opt = option.parse(parser.parse_args().opt)
    opt = option.dict_to_nonedict(opt)

    # initial configure
    scale = opt['scale']
    degrad = opt['degradation']
    network_opt = opt['networks']
    model_name = network_opt['which_model'].upper()
    if opt['self_ensemble']: model_name += 'plus'

    # create solver (and load model)
    solver = create_solver(opt)
    # Test phase
    print('===> Start Test')
    print("==================================================")
    print("Method: %s || Scale: %d || Degradation: %s" % (model_name, scale, degrad))

def loading_image(img):
    assert common.is_image_file(img), '[Error] [%s] is not a image file' % img

    # create test dataloader
    # bm_names = []
    # test_loaders = []

    # for _, dataset_opt in sorted(opt['datasets'].items()):
    #     test_set = create_dataset(dataset_opt)
    #     # test_set = dataset(dataset_opt)

    # while True:
    #     if os.path.exists("./test.py"):
    #         test_loader = create_dataloader(test_set, dataset_opt)
    #         try:
    #             test_loader = dataloader(test_set)
    #             test_loaders.append(test_loader)
    #             print('===> Test Dataset: [%s]   Number of images: [%d]' % (test_set.name(), len(test_set)))
    #             bm_names.append(test_set.name())
    #         except AssertionError as e:
    #             time.sleep(0.033)
    #         else:
    #             for bm, test_loader in zip(bm_names, test_loaders):
    #                 print("Test set : [%s]" % bm)
    #
    #                 sr_list = []
    #                 path_list = []
    #
    #                 need_HR = False if test_loader.dataset.__class__.__name__.find('LRHR') < 0 else True
    #                 for iter, batch in enumerate(test_loader):
    #                     execute(solver, batch, iter, bm, test_loader, need_HR, model_name,scale,sr_list, path_list, degrad);
    #

def execute(solver, batch,need_HR,):
    solver.feed_data(batch, need_HR=need_HR)

    # total_psnr = []
    # total_ssim = []
    total_time = []

    # calculate forward time
    t0 = time.time()
    solver.test()
    t1 = time.time()
    total_time.append((t1 - t0))

    visuals = solver.get_current_visual(need_HR=need_HR)
    # sr_list.append(visuals['SR'])
    print("Timer: %f sec ." % (t1 - t0))
    return visuals['SR']

    # for img, name in zip(sr_list, path_list):
    # calculate PSNR/SSIM metrics on Python
    # if need_HR:
    #     psnr, ssim = util.calc_metrics(visuals['SR'], visuals['HR'], crop_border=scale)
    #     total_psnr.append(psnr)
    #     total_ssim.append(ssim)
    #     path_list.append(os.path.basename(batch['HR_path'][0]).replace('HR', model_name))
    #     print("[%d/%d] %s || PSNR(dB)/SSIM: %.2f/%.4f || Timer: %.4f sec ." % (num + 1, len(test_loader),
    #                                                                            os.path.basename(batch['LR_path'][0]),
    #                                                                            psnr, ssim,
    #                                                                            (t1 - t0)))
    # else:
    #     path_list.append(os.path.basename(batch['LR_path'][0]))
    #     print("[%d/%d] %s || Timer: %.4f sec ." % (num + 1, len(test_loader),
    #                                                os.path.basename(batch['LR_path'][0]),
    #                                                (t1 - t0)))
    #
    # # save SR results for further evaluation on MATLAB
    # if need_HR:
    #     save_img_path = os.path.join('./results/SR/' + degrad, model_name, bm, "x%d" % scale)
    # else:
    #     save_img_path = os.path.join('./results/SR/' + bm, model_name, "x%d" % scale)
    #
    # print("===> Saving SR images of [%s]... Save Path: [%s]\n" % (bm, save_img_path))
    #
    # if not os.path.exists(save_img_path): os.makedirs(save_img_path)
    # for img, name in zip(sr_list, path_list):
    #     imageio.imwrite(os.path.join(save_img_path, name), img)

    print("==================================================")
    print("===> Finished !")


# if __name__ == '__main__':
#     main()
